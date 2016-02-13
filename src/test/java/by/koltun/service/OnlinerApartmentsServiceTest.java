package by.koltun.service;

import by.koltun.domain.to.ApartmentsPage;
import by.koltun.domain.to.realt.rent.ApartmentsRentPage;
import by.koltun.domain.to.realt.sale.ApartmentsSalePage;

import org.junit.Test;
import org.junit.Assert;
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

    @Test
    public void testGettingNumberOfTotalPages() {

        RestTemplate template = new RestTemplate();

        ApartmentsPage page = template.getForObject(RENT_LAST_CREATED_URL, ApartmentsRentPage.class);

        Assert.assertNotNull(page);
        Assert.assertNotNull(page.getPage());
        Assert.assertNotNull(page.getPage().getLast());

        page = template.getForObject(SALE_LAST_CREATED_URL, ApartmentsSalePage.class);

        Assert.assertNotNull(page);
        Assert.assertNotNull(page.getPage());
        Assert.assertNotNull(page.getPage().getLast());
    }
}
