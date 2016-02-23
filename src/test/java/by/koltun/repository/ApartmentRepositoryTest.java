package by.koltun.repository;

import by.koltun.Application;
import by.koltun.domain.Apartment;
import by.koltun.domain.Location;
import by.koltun.domain.Price;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import static org.junit.Assert.*;

/**
 * {@link ApartmentRepository} test
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@Transactional
public class ApartmentRepositoryTest {

    private static final String DEFAULT_APARTMENT_URL = "http://junit.org";

    private static final BigDecimal DEFAULT_PRICE_RUBLE = BigDecimal.valueOf(5000000);
    private static final BigDecimal DEFAULT_PRICE_USD = BigDecimal.valueOf(250);

    private static final String DEFAULT_ADDRESS = "Minsk, Nezavisimosti ave";
    private static final Double DEFAULT_LATITUDE = 154.91929;
    private static final Double DEFAULT_LONGITUDE = 154.9132;

    private Location location;

    @Autowired ApartmentRepository apartmentRepository;
    @Autowired LocationRepository locationRepository;

    @Before
    public void beforeTest() {

        location = new Location();
        location.setAddress(DEFAULT_ADDRESS);
        location.setLatitude(DEFAULT_LATITUDE);
        location.setLongitude(DEFAULT_LONGITUDE);

        location = locationRepository.save(location);
    }


    @After
    public void afterTest() {

    }

    @Test
    public void testSaveOneWithoutLocationAndPrices() {

        Apartment apartment = new Apartment();

        apartment.setUrl(DEFAULT_APARTMENT_URL);
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        apartment.setUpdated(zonedDateTime);
        apartment.setCreated(zonedDateTime);
        apartment.setApartmentId(1L);

        apartment.setLocation(location);

        Price price = new Price();
        price.setUpdated(ZonedDateTime.now());
        price.setPriceRuble(DEFAULT_PRICE_RUBLE);
        price.setPriceUsd(DEFAULT_PRICE_USD);
        price.setApartment(apartment);

        apartment.getPrices().add(price);

        apartment = apartmentRepository.save(apartment);
        assertNotNull(apartment);
        assertNotNull(apartment.getId());

        assertEquals(1, apartment.getPrices().size());
        assertTrue("Saved price is not equals created.",
            apartment.getPrices().contains(price));

        assertNotNull(apartment.getLocation());
        assertEquals(location.getAddress(), apartment.getLocation().getAddress());
        assertEquals(location.getLatitude(), apartment.getLocation().getLatitude());
        assertEquals(location.getLongitude(), apartment.getLocation().getLongitude());

        apartment.setUpdated(ZonedDateTime.now());

        apartment = apartmentRepository.save(apartment);
        assertNotNull(apartment);
        assertTrue(!zonedDateTime.equals(apartment.getUpdated()));
    }
}
