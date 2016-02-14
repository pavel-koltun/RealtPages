package by.koltun.repository;

import by.koltun.Application;
import by.koltun.domain.ApartmentRent;
import by.koltun.domain.Location;
import by.koltun.domain.Price;
import by.koltun.domain.enumeration.RentType;
import org.elasticsearch.common.inject.Inject;
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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@Transactional
public class ApartmentRentRepositoryTest {

    @Autowired private ApartmentRentRepository apartmentRentRepository;

    @Test
    public void testRepositoryPersist() {

        ApartmentRent apartmentRent = new ApartmentRent();

        apartmentRent.setApartmentId(1L);
        apartmentRent.setUrl("testApartmentRentUrl");

        Location location = new Location();
        location.setAddress("testRentAddress");
        location.setLatitude(1D);
        location.setLongitude(1D);
        apartmentRent.setLocation(location);

        Price price = new Price();
        price.setPriceUsd(BigDecimal.TEN);
        price.setPriceRuble(BigDecimal.ONE);
        apartmentRent.setPrice(price);

        apartmentRent.setCreated(ZonedDateTime.now());
        apartmentRent.setUpdated(ZonedDateTime.now());

        apartmentRent.setIsOwner(Boolean.TRUE);
        apartmentRent.setType(RentType.ROOM);

        apartmentRent = apartmentRentRepository.save(apartmentRent);

        assertNotNull(apartmentRent);
    }
}
