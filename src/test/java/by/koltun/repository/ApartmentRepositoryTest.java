package by.koltun.repository;

import by.koltun.Application;
import by.koltun.domain.*;
import by.koltun.domain.ApartmentSale;
import by.koltun.service.onliner.ApartmentService;
import by.koltun.service.onliner.ZonedDateTimeConverter;
import by.koltun.service.onliner.converter.LocationConverter;
import by.koltun.service.onliner.converter.PriceConverter;
import by.koltun.service.onliner.rent.converter.ApartmentRentConverter;
import by.koltun.service.onliner.sale.converter.ApartmentSaleConverter;
import by.koltun.web.onliner.ApartmentRestTest;
import by.koltun.web.rest.to.rent.ApartmentRentPage;
import by.koltun.web.rest.to.sale.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired ApartmentRentRepository apartmentRentRepository;
    @Autowired ApartmentSaleRepository apartmentSaleRepository;
    @Autowired LocationRepository locationRepository;
    @Autowired RestTemplate restTemplate;

    @Autowired ApartmentService apartmentService;
    @Autowired ZonedDateTimeConverter zonedDateTimeConverter;

    @Autowired PriceConverter priceConverter;
    @Autowired LocationConverter locationConverter;

    @Autowired ApartmentRentConverter apartmentRentConverter;
    @Autowired ApartmentSaleConverter apartmentSaleConverter;

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
    public void testSaveOne() {

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

    @Test
    public void testSaveApartmentRentList() {

        ApartmentRentPage page = apartmentService.getPage(ApartmentRestTest.URL_RENT, ApartmentRentPage.class);
        assertNotNull(page);

        List<by.koltun.web.rest.to.rent.ApartmentRent> list = page.getApartments();
        assertNotNull(list);

        Set<ApartmentRent> apartments = list.stream()
            .map(apartmentRentConverter::convert)
            .collect(Collectors.toSet());

        assertNotNull(apartments);
        assertTrue(apartments.size() == list.size());

        List<ApartmentRent> saved = apartmentRepository.save(apartments);
        apartmentRepository.flush();
        assertNotNull(saved);

        saved.stream()
            .map(Apartment::getId)
            .forEach(Assert::assertNotNull);

        by.koltun.web.rest.to.rent.ApartmentRent apartmentUpdated = list.stream().findAny().get();
        ZonedDateTime updateDate = zonedDateTimeConverter.toZonedDateTime(new Date()).withNano(0);

        by.koltun.web.rest.to.Price priceUpdated = apartmentUpdated.getPrice();
        priceUpdated.setPriceUsd(priceUpdated.getPriceUsd().subtract(BigDecimal.TEN));
        priceUpdated.setPriceRuble(priceUpdated.getPriceRuble().subtract(BigDecimal.TEN.multiply(BigDecimal.valueOf(100))));

        Price price = priceConverter.convert(priceUpdated);

        ApartmentRent existingApartment = apartmentRentRepository.findByApartmentId(apartmentUpdated.getApartmentId()).get();
        assertTrue(existingApartment.getPrices().size() == 1);

        price.setApartment(existingApartment);
        price.setUpdated(updateDate);

        existingApartment.getPrices().add(price);
        existingApartment.setUpdated(updateDate);

        assertTrue(existingApartment.getPrices().size() == 2);

        existingApartment = apartmentRentRepository.save(existingApartment);
        apartmentRentRepository.flush();
        assertNotNull(existingApartment);
        assertTrue(existingApartment.getPrices().size() == 2);

        assertTrue(existingApartment.getUpdated().isEqual(updateDate));
    }

    @Test
    public void testSaveApartmentSaleList() {

        ApartmentSalePage page = apartmentService.getPage(ApartmentRestTest.URL_SALE, ApartmentSalePage.class);
        assertNotNull(page);

        List<by.koltun.web.rest.to.sale.ApartmentSale> list = page.getApartments();
        assertNotNull(list);

        Set<ApartmentSale> apartments = list.stream()
            .map(apartmentSaleConverter::convert)
            .collect(Collectors.toSet());
        assertNotNull(apartments);
        assertTrue(apartments.size() == list.size());

        List<ApartmentSale> savedApartments = apartmentSaleRepository.save(apartments);
        apartmentSaleRepository.flush();
        assertNotNull(savedApartments);

        savedApartments.stream()
            .map(Apartment::getId)
            .forEach(Assert::assertNotNull);
    }

    @Test
    public void testDateConversion() {

        final Date date = new Date();

        final ZonedDateTime zonedDateTime = zonedDateTimeConverter.toZonedDateTime(date);
        assertNotNull(zonedDateTime);

        final Date dateFromZonedDateTime = zonedDateTimeConverter.toDate(zonedDateTime);
        assertNotNull(dateFromZonedDateTime);
        assertTrue(date.equals(dateFromZonedDateTime));

        final ZonedDateTime zonedDateTimeFromDate = zonedDateTimeConverter.toZonedDateTime(dateFromZonedDateTime);
        assertNotNull(zonedDateTimeFromDate);
        assertTrue(zonedDateTime.equals(zonedDateTimeFromDate));
    }
}
