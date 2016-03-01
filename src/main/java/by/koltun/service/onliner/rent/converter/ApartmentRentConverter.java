package by.koltun.service.onliner.rent.converter;

import by.koltun.domain.ApartmentRent;
import by.koltun.domain.enumeration.RentType;
import by.koltun.service.onliner.converter.AbstractApartmentConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * {@link ApartmentRent} converter
 */
@Service
public class ApartmentRentConverter extends AbstractApartmentConverter<by.koltun.web.rest.to.rent.ApartmentRent, ApartmentRent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApartmentRentConverter.class);

    public ApartmentRentConverter() {

        super(ApartmentRent.class);
    }

    @Override
    public ApartmentRent convert(by.koltun.web.rest.to.rent.ApartmentRent apartment) {

        try {
            ApartmentRent result = super.convert(apartment);

            Objects.requireNonNull(result, "Converted apartment is null");
            Objects.requireNonNull(apartment.getContact(), "ApartmentRent contact is null");
            Objects.requireNonNull(apartment.getContact().getOwner(), "ApartmentRent contact owner property is null");

            Objects.requireNonNull(apartment.getRentType(), "ApartmentRent rent type is null");

            result.setOwner(apartment.getContact().getOwner());
            result.setType(RentType.fromCode(apartment.getRentType()));

            return result;
        } catch (NullPointerException e) {

            LOGGER.error("Failed to convert apartment", apartment, e);
        }

        return null;
    }
}
