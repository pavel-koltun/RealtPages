package by.koltun.service.apartment;

import by.koltun.domain.to.ApartmentsPage;
import by.koltun.domain.to.realt.rent.ApartmentRent;
import by.koltun.domain.to.realt.rent.ApartmentsRentPage;
import by.koltun.domain.to.realt.sale.ApartmentsSalePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.method.P;
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

    public void getLastUpdatedSaleApartments() {

        String url = env.getProperty("onliner.sale.urls.lastUpdated");

        if (url == null) {

            LOGGER.error("Empty service url. Check application properties file.");
            return;
        }

        try {
            RestTemplate template = new RestTemplate();

            ApartmentsSalePage page = template.getForObject(url, ApartmentsSalePage.class);

            LOGGER.info("The Sale page: {}", page);
        } catch (RestClientException e) {

            LOGGER.error("An exception occurred while consuming {}", url);
            LOGGER.error("Exception is:", e);
        }
    }

    public void getLastCreatedRentApartments() throws InterruptedException {

        String url = env.getProperty("onliner.rent.urls.lastCreated");

        if (url == null) {

            LOGGER.error("Empty service url. Check application properties file.");
            return;
        }

        LOGGER.info("The update of last created rent apartments started.");

        Optional<ApartmentsRentPage> pageWithInfo = getPage(url, 1, ApartmentsRentPage.class);

        if (pageWithInfo.isPresent()) {

            int totalNumberOfElements = pageWithInfo.get().getTotal();

            //TODO remove. Test feature.
            int totalNumberOfPages = 10;// pageWithInfo.get().getPage().getLast();
            int pageNumber = totalNumberOfPages;

            if (totalNumberOfPages > 0) {

                Set<ApartmentRent> rentApartments = new HashSet<>();

                do {

                    LOGGER.info("Processing {} rent page.", pageNumber);

                    Optional<ApartmentsRentPage> page = getPage(url, pageNumber, ApartmentsRentPage.class);

                    if (page.isPresent()) {

                        rentApartments.addAll(page.get().getApartments());

                        int updatedTotalNumberOfElements = page.get().getTotal();

                        int numberOfAddedApartments = updatedTotalNumberOfElements - totalNumberOfElements;

                        if (numberOfAddedApartments > 0) {

                            int numberOfPagesToReadAgain = 1 + numberOfAddedApartments % page.get().getPage().getLimit();

                            pageNumber += numberOfPagesToReadAgain;

                        } else {

                            pageNumber--;
                        }

                        totalNumberOfElements = updatedTotalNumberOfElements;
                    } else {

                        pageNumber--;
                    }

                    TimeUnit.SECONDS.sleep(10);
                } while (pageNumber > 0);

                LOGGER.info("Total apartments found: {}", rentApartments.size());
            } else {

                LOGGER.error("There are no pages found with {}", url);
            }
        }
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
