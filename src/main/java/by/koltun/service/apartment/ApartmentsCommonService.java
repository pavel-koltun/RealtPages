package by.koltun.service.apartment;

import by.koltun.domain.to.ApartmentsPage;
import by.koltun.domain.to.realt.rent.ApartmentsRentPage;
import by.koltun.domain.to.realt.sale.ApartmentsSalePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;

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

    public void getLastCreatedRentApartments() {

        String url = env.getProperty("onliner.rent.urls.lastCreated");

        if (url == null) {

            LOGGER.error("Empty service url. Check application properties file.");
            return;
        }

        try {

            int numberOfPages = getNumberOfPages(url, ApartmentsRentPage.class);

            if (numberOfPages > 0) {

                ApartmentsRentPage page = getPage(url, 1, ApartmentsRentPage.class);

                LOGGER.info("The Rent page: {}", page);
                LOGGER.info("Total pages: {}", numberOfPages);
            } else {

                LOGGER.error("Unable to get number of pages from {}", url);
            }
        } catch (RestClientException e) {

            LOGGER.error("An exception occurred while consuming {}", url);
            LOGGER.error("Exception is:", e);
        }
    }

    /**
     * Получение общего количества страниц.
     * @param url Адрес сервиса аренды {@link ApartmentsRentPage} либо продажи {@link ApartmentsSalePage}.
     * @return Число страниц, либо {@code 0} если число страниц не может быть определено.
     */
    private <T extends ApartmentsPage> int getNumberOfPages(String url, Class<T> clazz) {

        if (url == null) {

            throw new IllegalArgumentException("Url can't be null.");
        }

        int result = 0;

        try {

            RestTemplate restTemplate = new RestTemplate();

            ApartmentsPage apartmentsPage = restTemplate.getForObject(url, clazz);

            if (apartmentsPage != null && apartmentsPage.getPage() != null) {

                result = apartmentsPage.getPage().getLast();
            }
        } catch (RestClientException e) {

            LOGGER.error("An exception occurred while consuming {}", url);
            LOGGER.error("Exception is:", e);
        }

        return result;
    }

    private <T extends ApartmentsPage> T getPage(String url, int pageNumber, Class<T> clazz) {

        if (url == null) {

            throw new IllegalArgumentException("Url can't be null.");
        }

        T page = null;

        try {

            RestTemplate template = new RestTemplate();

            page = template.getForObject(url, clazz);
        } catch (RestClientException e) {

            LOGGER.error("Failed to get page number {} from url: {}", pageNumber, url);
            LOGGER.error("Exception is:", e);
        }

        return page;
    }
}
