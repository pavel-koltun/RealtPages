package by.koltun.service.onliner;

import by.koltun.web.rest.to.ApartmentPage;
import by.koltun.web.rest.to.rent.ApartmentRentPage;
import by.koltun.web.rest.to.sale.ApartmentSalePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Service to receive information from Onliner service
 */
@Service
public class ApartmentService {

    @Bean
    public RestTemplate restTemplate() {

        return new RestTemplate();
    }

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Real estate listing
     * @param url link
     * @param pageNumber number of page to receive
     * @param tClass {@link ApartmentRentPage} or {@link ApartmentSalePage} class
     * @return {@link ApartmentPage} with one of {@link ApartmentRentPage} or {@link ApartmentSalePage}
     */
    public <T extends ApartmentPage> T getPage(String url, int pageNumber, Class<T> tClass) {

        url = url.concat(String.valueOf(pageNumber));

        return getPage(url, tClass);
    }

    /**
     * Real estate listing
     * @param url link
     * @param tClass {@link ApartmentRentPage} or {@link ApartmentSalePage} class
     * @return {@link ApartmentPage} with one of {@link ApartmentRentPage} or {@link ApartmentSalePage}
     */
    public <T extends ApartmentPage> T getPage(String url, Class<T> tClass) {

        return restTemplate.getForObject(url, tClass);
    }
}
