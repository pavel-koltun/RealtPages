package by.koltun.service.onliner.converter;

import by.koltun.domain.Location;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * {@link by.koltun.web.rest.to.Location} to {@link Location} converter
 */
@Service
public class LocationConverter implements DomainModelConverter<by.koltun.web.rest.to.Location, Location> {

    /**
     * Method which converts location response from remote service to application domain model object
     * @param location Location response from remote service
     * @return Location object
     * @throws NullPointerException if location or one of the location address, latitude, longitude is null
     */
    @Override
    public Location convert(by.koltun.web.rest.to.Location location) {

        Objects.requireNonNull(location, "Location is null");
        Objects.requireNonNull(location.getAddress(), "Location address is null");
        Objects.requireNonNull(location.getLatitude(), "Location latitude is null");
        Objects.requireNonNull(location.getLongitude(), "Location longitude is null");


        Location result = new Location();
        result.setAddress(location.getAddress());
        result.setLatitude(location.getLatitude());
        result.setLongitude(location.getLongitude());

        return result;
    }
}
