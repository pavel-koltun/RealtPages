package by.koltun.service.apartment;

import by.koltun.domain.ApartmentRent;
import by.koltun.domain.Location;
import by.koltun.domain.Price;
import by.koltun.domain.enumeration.RentType;
import by.koltun.repository.LocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by koltun on 2/14/16.
 */
@Service
public class ApartmentsConverterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApartmentsConverterService.class);

    @Autowired private LocationRepository locationRepository;

    public Set<ApartmentRent> convertRentApartments(Set<by.koltun.domain.to.realt.rent.ApartmentRent> list) {

        Set<ApartmentRent> result = Collections.emptySet();

        if (list != null) {

            result = list.stream()
                .map(this::convertRentApartment)
                .collect(Collectors.toSet());

        }

        return result;
    }

    public ApartmentRent convertRentApartment(by.koltun.domain.to.realt.rent.ApartmentRent a) {

        ApartmentRent apartment = new ApartmentRent();

        apartment.setApartmentId(a.getApartmentId());
        apartment.setCreated(convertDateToZonedDateTime(a.getCreated()));
        apartment.setUpdated(convertDateToZonedDateTime(a.getUpdated()));
        apartment.setLocation(new Location(a.getLocation()));
        apartment.setPrice(new Price(a.getPrice(), convertDateToZonedDateTime(a.getUpdated()), apartment));
        apartment.setUrl(a.getUrl());

        apartment.setType(determineRentType(a.getRentType()));
        apartment.setIsOwner(a.getContact().getOwner());

        return apartment;
    }

    public ZonedDateTime convertDateToZonedDateTime(Date date) {

        return date.toInstant().atZone(ZoneId.of("UTC"));
    }

    public RentType determineRentType(String rentType) {

        if (rentType == null) {

            return RentType.UNKNOWN;
        }

        RentType result = RentType.UNKNOWN;

        switch (rentType) {
            case "room":
                result = RentType.ROOM;
                break;
            case "1_room":
                result = RentType.ONE_ROOM;
                break;
            case "2_rooms":
                result = RentType.TWO_ROOMS;
                break;
            case "3_rooms":
                result = RentType.THREE_ROOMS;
                break;
            case "4_rooms":
                result = RentType.FOUR_ROOMS;
                break;
            case "5_rooms":
                result = RentType.FIVE_ROOMS;
                break;
            case "6_rooms":
                result = RentType.SIX_ROOMS;
                break;
            default:
                LOGGER.warn("Unknown rent type found: {}", rentType);
                break;
        }

        return result;
    }
}
