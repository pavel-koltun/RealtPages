package by.koltun.service;

import by.koltun.Application;
import by.koltun.domain.to.realt.rent.ApartmentsRentPage;
import by.koltun.domain.to.realt.sale.ApartmentsSalePage;

import org.elasticsearch.common.inject.Inject;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

/**
 * Тест для проверки доступности и корректности внешних сервисов
 */
public class OnlinerApartmentsServiceTest {

    public static final String SALE_LAST_CREATED_URL = "https://pk.api.onliner.by/search/apartments?order=created_at:desc";
    public static final String SALE_LAST_UPDATED_URL = "https://pk.api.onliner.by/search/apartments";
    public static final String RENT_LAST_CREATED_URL = "https://ak.api.onliner.by/search/apartments?order=created_at:desc";
    public static final String RENT_LAST_UPDATED_URL = "https://ak.api.onliner.by/search/apartments";

    @Test
    public void testSaleRemoteService() {

        RestTemplate template = new RestTemplate();

        ApartmentsSalePage page = template.getForObject(SALE_LAST_UPDATED_URL, ApartmentsSalePage.class);

        Assert.assertNotNull(page);
    }

    @Test
    public void testRentRemoteService() {

        RestTemplate template = new RestTemplate();

        ApartmentsRentPage page = template.getForObject(RENT_LAST_UPDATED_URL, ApartmentsRentPage.class);

        Assert.assertNotNull(page);
    }
}
