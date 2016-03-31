package by.koltun.repository;

import by.koltun.OnlinerRealtPagesApp;
import by.koltun.OnlinerRealtPagesApp;
import by.koltun.domain.Location;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

/**
 * {@link LocationRepository} test
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = OnlinerRealtPagesApp.class)
@WebAppConfiguration
@IntegrationTest
@Transactional
public class LocationRepositoryTest {

    @Autowired LocationRepository locationRepository;

    private static final String DEFAULT_LOCATION_ADDRESS = "пр-т Независимости, 180";
    private static final Double DEFAULT_LOCATION_LATITUDE = 132.33;
    private static final Double DEFAULT_LOCATION_LONGITUDE = 134.25;

    @Test
    public void testSaveLocation() {

        Location location = new Location();

        location.setAddress(DEFAULT_LOCATION_ADDRESS);
        location.setLatitude(DEFAULT_LOCATION_LATITUDE);
        location.setLongitude(DEFAULT_LOCATION_LONGITUDE);

        location = locationRepository.save(location);

        assertNotNull(location.getId());

        assertEquals(DEFAULT_LOCATION_ADDRESS, location.getAddress());
        assertEquals(DEFAULT_LOCATION_LATITUDE, location.getLatitude());
        assertEquals(DEFAULT_LOCATION_LONGITUDE, location.getLongitude());
    }
}
