package by.koltun.service.apartment;

import by.koltun.domain.to.Apartment;
import by.koltun.domain.to.ApartmentsPage;
import by.koltun.domain.to.realt.rent.ApartmentRent;
import by.koltun.domain.to.realt.rent.ApartmentsRentPage;
import by.koltun.repository.ApartmentRentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Сервис, реализующий запрос на rest-сервер
 */
@Component
public class ApartmentsCommonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApartmentsCommonService.class);

    @Inject private Environment env;

    @Inject private ApartmentsConverterService apartmentsConverterService;
    @Inject private ApartmentSaveService apartmentSaveService;

    @Inject private ApartmentRentRepository apartmentRentRepository;

    public void getLastUpdatedRentApartments() throws InterruptedException {

        String url = env.getProperty("onliner.rent.urls.lastUpdated");

        if (url == null) {

            LOGGER.error("Empty service url. Check application properties file.");
            return;
        }

        LOGGER.info("[Update of rent apartments] Started.");

        Optional<by.koltun.domain.ApartmentRent> latestUpdatedApartment = apartmentRentRepository.findTopByOrderByUpdatedDesc();
        Optional<Date> dateToSearch = Optional.empty();

        if (latestUpdatedApartment.isPresent()) {

            dateToSearch = Optional.of(Date.from(latestUpdatedApartment.get().getUpdated().toInstant()));
        } else {

            Optional<ApartmentsRentPage> page = getPage(url, 1, ApartmentsRentPage.class);

            if (page.isPresent()) {

                page = getPage(url, page.get().getPage().getLast(), ApartmentsRentPage.class);

                if (page.isPresent()) {

                    dateToSearch = page.get().getApartments().stream()
                        .map(Apartment::getUpdated)
                        .min(Date::compareTo);
                }
            }
        }

        if (!dateToSearch.isPresent()) {

            LOGGER.warn("[Update of rent apartments] Unable to determine date to update apartments");
            return;
        }

        Set<ApartmentRent> rentApartments = new HashSet<>();

        Optional<Date> date;
        int pageNumber = 1;

        do {

            Optional<ApartmentsRentPage> page = getPage(url, pageNumber, ApartmentsRentPage.class);

            if (page.isPresent()) {

                date = page.get().getApartments().stream()
                    .map(Apartment::getUpdated)
                    .min(Date::compareTo);

                if (!date.isPresent()) {

                    LOGGER.warn("[Update of rent apartments] Unable to determine date to update apartments");
                    break;
                }

                rentApartments.addAll(page.get().getApartments());

                LOGGER.info("[Update of rent apartments] Processed page: {}\n" +
                    "\tOldest date: {}\n" +
                    "\tNewest date: {}",
                    pageNumber, date, dateToSearch);

                pageNumber++;

                TimeUnit.SECONDS.sleep(2);
            } else {

                LOGGER.warn("[Update of rent apartments] Empty response from {} page", pageNumber);
                break;
            }
        } while (date.get().compareTo(dateToSearch.get()) > 0);

        LOGGER.info("[Update of rent apartments] Found: {}", rentApartments.size());

        Set<by.koltun.domain.ApartmentRent> apartmentsToSave =
            apartmentsConverterService.convertRentApartments(rentApartments);

        apartmentSaveService.saveRentApartments(apartmentsToSave);
    }

    private <T extends ApartmentsPage> Optional<T> getPage(String url, int pageNumber, Class<T> clazz) {

        if (url == null) {

            throw new IllegalArgumentException("Url can't be null.");
        }

        T result = null;

        try {

            RestTemplate template = new RestTemplate();

            result = template.getForObject(url + pageNumber, clazz);
        } catch (RestClientException e) {

            LOGGER.error("Failed to get page number {} from url: {}", pageNumber, url);
            LOGGER.error("Exception is:", e);
        }

        return Optional.ofNullable(result);
    }
}
