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

    public void getLastSaleApartments() {

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

            LOGGER.error("An exception occurred", e);
        }
    }

    public void getLastRentApartments() {

        String url = env.getProperty("onliner.rent.urls.lastUpdated");

        if (url == null) {

            LOGGER.error("Empty service url. Check application properties file.");
            return;
        }

        try {
            RestTemplate template = new RestTemplate();

            ApartmentsRentPage page = template.getForObject(url, ApartmentsRentPage.class);

            LOGGER.info("The Sale page: {}", page);
        } catch (RestClientException e) {

            LOGGER.error("An exception occurred", e);
        }
    }
}
