package by.koltun.service.onliner.converter;

import by.koltun.domain.Apartment;
import by.koltun.domain.Location;
import by.koltun.domain.Price;
import by.koltun.repository.LocationRepository;
import by.koltun.service.onliner.ZonedDateTimeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Objects;
import java.util.Optional;

/**
 * {@link by.koltun.web.rest.to.Apartment} to {@link Apartment} converter
 */
public abstract class AbstractApartmentConverter<T extends by.koltun.web.rest.to.Apartment, R extends Apartment> implements DomainModelConverter<T, R> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractApartmentConverter.class);

    @Autowired LocationRepository locationRepository;

    @Autowired @Qualifier("locationConverter") DomainModelConverter<by.koltun.web.rest.to.Location, Location> locationConverter;
    @Autowired @Qualifier("priceConverter") DomainModelConverter<by.koltun.web.rest.to.Price, Price> priceConverter;
    @Autowired ZonedDateTimeConverter zonedDateTimeConverter;

    private Class<R> type;

    public AbstractApartmentConverter(Class<R> type) {

        this.type = type;
    }

    @Override
    public R convert(T apartment) {

        Objects.requireNonNull(apartment, "Apartment is null");

        Objects.requireNonNull(apartment.getApartmentId(), "Apartment id is null");
        Objects.requireNonNull(apartment.getCreated(), "Created is null");
        Objects.requireNonNull(apartment.getCreated(), "Created is null");

        Price price = priceConverter.convert(apartment.getPrice());
        Location location = locationConverter.convert(apartment.getLocation());

        R result = null;

        try {

            result = type.newInstance();

        } catch (IllegalAccessException | InstantiationException e) {

            LOGGER.error("Failed to create {} instance", type.getTypeName());
            return null;
        }

        result.setApartmentId(apartment.getApartmentId());
        result.setCreated(zonedDateTimeConverter.toZonedDateTime(apartment.getCreated()));
        result.setUpdated(zonedDateTimeConverter.toZonedDateTime(apartment.getUpdated()));
        result.setUrl(apartment.getUrl());

        price.setUpdated(result.getUpdated());
        price.setApartment(result);

        result.getPrices().add(price);

        Optional<Location> existedLocation = locationRepository.findByAddress(apartment.getLocation().getAddress());

        if (!existedLocation.isPresent()) {

            existedLocation = Optional.of(location);
        }

        result.setLocation(existedLocation.get());

        return result;
    }
}
