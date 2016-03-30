package by.koltun.service.onliner.rent;

import by.koltun.repository.ApartmentRentRepository;
import by.koltun.service.onliner.ZonedDateTimeConverter;
import by.koltun.service.onliner.converter.DomainModelConverter;
import by.koltun.web.rest.to.Apartment;
import by.koltun.web.rest.to.rent.ApartmentRent;
import by.koltun.web.rest.to.rent.ApartmentRentPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * {@link by.koltun.domain.ApartmentRent} update service
 */
@Service
public class ApartmentRentUpdateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApartmentRentUpdateService.class);

    //TODO to properties file
    private static final String URL = "https://ak.api.onliner.by/search/apartments?page=";

    @Autowired ApartmentSaveService     apartmentSaveService;
    @Autowired @Qualifier("apartmentRentConverter")
    DomainModelConverter<ApartmentRent, by.koltun.domain.ApartmentRent> apartmentRentConverter;
    @Autowired ApartmentRentRepository  apartmentRentRepository;
    @Autowired RestTemplate             restTemplate;
    @Autowired ZonedDateTimeConverter   zonedDateTimeConverter;

    @Bean
    public RestTemplate restTemplate() {

        return new RestTemplate();
    }

    public List<ApartmentRent> getRentApartments() {

        Date updateTo = getDateToUpdate();

        Date minApartmentUpdateDateOnPage;

        int pageNumber = 1;

        List<ApartmentRent> result = new ArrayList<>();

        try {
            do {

                LOGGER.info("[ApartmentRentUpdateService] Processing page: {}", pageNumber);

                List<ApartmentRent> apartmentsOnPage = getRentApartmentsOnPage(pageNumber);

                if (apartmentsOnPage.isEmpty()) {

                    LOGGER.warn("[ApartmentRentUpdateService] Apartments not found on page {}", pageNumber);
                    break;
                }

                Optional<Date> minDate = apartmentsOnPage.stream()
                    .min(Comparator.comparing(Apartment::getUpdated))
                    .map(Apartment::getUpdated);

                if (!minDate.isPresent()) {

                    LOGGER.warn("Minimal date not found on page {}", pageNumber);
                    break;
                }

                minApartmentUpdateDateOnPage = minDate.get();

                result.addAll(apartmentsOnPage);

                pageNumber++;

                TimeUnit.MILLISECONDS.sleep(1500);
            } while (minApartmentUpdateDateOnPage.compareTo(updateTo) > 0);
        } catch (InterruptedException e) {

            result = Collections.emptyList();
            LOGGER.error("[ApartmentRentUpdateService] Execution was interrupted. Update was cancelled.");
        }

        if (!result.isEmpty()) {

            apartmentSaveService.saveOrUpdateRentApartments(result.stream()
                .map(apartmentRentConverter::convert)
                .collect(Collectors.toSet()));
        }

        return result;
    }

    private List<ApartmentRent> getRentApartmentsOnPage(int pageNumber) {

        List<ApartmentRent> result = Collections.emptyList();

        try {

            ApartmentRentPage page = restTemplate.getForObject(URL + pageNumber, ApartmentRentPage.class);

            if (page != null && page.getApartments() != null) {

                result = page.getApartments();
            }
        } catch (Exception e) {

            LOGGER.error("Failed to fetch rent apartments from page {}", pageNumber, e);
        }

        return result;
    }

    private Date getDateToUpdate() {

        Optional<by.koltun.domain.ApartmentRent> lastUpdatedApartmentRent = apartmentRentRepository.findTopByOrderByUpdatedDesc();

        ZonedDateTime date = ZonedDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT, ZoneId.systemDefault());

        if (lastUpdatedApartmentRent.isPresent()) {

            date = lastUpdatedApartmentRent.get().getUpdated();
        }

        return Date.from(date.toInstant());
    }
}
