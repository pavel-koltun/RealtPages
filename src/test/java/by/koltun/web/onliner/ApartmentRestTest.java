package by.koltun.web.onliner;

import by.koltun.Application;
import by.koltun.web.rest.to.*;
import by.koltun.web.rest.to.rent.ApartmentRent;
import by.koltun.web.rest.to.rent.ApartmentRentPage;
import by.koltun.web.rest.to.rent.Contact;
import by.koltun.web.rest.to.sale.ApartmentSale;
import by.koltun.web.rest.to.sale.ApartmentSalePage;
import by.koltun.web.rest.to.sale.Area;
import by.koltun.web.rest.to.sale.Seller;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests to check work with remote service
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ApartmentRestTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApartmentRestTest.class);

    public static final String URL_RENT = "https://ak.api.onliner.by/search/apartments";
    public static final String URL_SALE = "https://pk.api.onliner.by/search/apartments";

    @Configuration
    static class Config {

        @Bean
        public RestTemplate restTemplate() {

            return new RestTemplate();
        }
    }

    @Autowired private RestTemplate restTemplate;

    @Test
    public void testGetApartmentRentPage() {

        ApartmentRentPage page = restTemplate.getForObject(URL_RENT, ApartmentRentPage.class);
        testApartmentPage(page);

        testPageInfo(page.getPageInfo());

        assertNotNull(page.getApartments());

        List<ApartmentRent> apartments = page.getApartments();
        assertNotNull(apartments);
        assertFalse(apartments.isEmpty());

        apartments.stream().forEach(
            apartment -> {
                testApartment(apartment);
                testPrice(apartment.getPrice());
                testLocation(apartment.getLocation());

                assertNotNull(apartment.getRentType());

                Contact contact = apartment.getContact();
                LOGGER.info("{}", contact);
                assertNotNull(contact);
                assertNotNull(contact.getOwner());
            }
        );
    }

    @Test
    public void testGetApartmentSalePage() {

        ApartmentSalePage page = restTemplate.getForObject(URL_SALE, ApartmentSalePage.class);
        testApartmentPage(page);

        testPageInfo(page.getPageInfo());

        assertNotNull(page.getApartments());

        List<ApartmentSale> apartments = page.getApartments();
        assertNotNull(apartments);
        assertFalse(apartments.isEmpty());

        apartments.stream().forEach(
            apartment -> {
                testApartment(apartment);
                testPrice(apartment.getPrice());
                testLocation(apartment.getLocation());

                assertNotNull(apartment.getFloor());
                assertNotNull(apartment.getFloors());
                assertNotNull(apartment.getResale());
                assertNotNull(apartment.getRooms());

                Seller seller = apartment.getSeller();
                LOGGER.info("{}", seller);
                assertNotNull(seller.getType());

                Area area = apartment.getArea();
                LOGGER.info("{}", area);
                assertNotNull(area);
                assertNotNull(area.getKitchen());
                assertNotNull(area.getLiving());
                assertNotNull(area.getTotal());
            }
        );
    }

    private void testApartmentPage(ApartmentPage page) {

        LOGGER.info("{}", page);
        assertNotNull(page);
        assertNotNull(page.getTotal());
    }

    private void testPageInfo(PageInfo pageInfo) {

        LOGGER.info("{}", pageInfo);
        assertNotNull(pageInfo);
        assertNotNull(pageInfo.getCurrent());
        assertNotNull(pageInfo.getLast());
        assertNotNull(pageInfo.getItems());
        assertNotNull(pageInfo.getLimit());
    }

    private void testApartment(Apartment apartment) {

        LOGGER.info("{}", apartment);
        assertNotNull(apartment.getApartmentId());
        assertNotNull(apartment.getCreated());
        assertNotNull(apartment.getUpdated());
        assertNotNull(apartment.getUrl());
    }

    private void testLocation(Location location) {

        LOGGER.info("{}", location);
        assertNotNull(location);
        assertNotNull(location.getAddress());
        assertNotNull(location.getLatitude());
        assertNotNull(location.getLongitude());
    }

    private void testPrice(Price price) {

        LOGGER.info("{}", price);
        assertNotNull(price);
        assertNotNull(price.getPriceUsd());
        assertNotNull(price.getPriceRuble());
    }
}
