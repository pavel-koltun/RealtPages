package by.koltun.service.onliner.rent;

import by.koltun.domain.*;
import by.koltun.repository.ApartmentRentRepository;
import by.koltun.repository.ApartmentSaleRepository;
import by.koltun.repository.LocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by koltun on 3/2/16.
 */
@Service
public class ApartmentSaveService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApartmentSaveService.class);

    @Autowired ApartmentRentRepository apartmentRentRepository;
    @Autowired ApartmentSaleRepository apartmentSaleRepository;
    @Autowired LocationRepository locationRepository;

    public List<ApartmentRent> saveOrUpdateRentApartments(Set<ApartmentRent> apartments) {

        Set<ApartmentRent> existingInDatabase = apartmentRentRepository.findByApartmentIdIn(apartments.stream()
            .map(Apartment::getApartmentId)
            .collect(Collectors.toSet())
        );

        Map<Boolean, Set<ApartmentRent>> apartmentsByExisting = apartments.stream()
            .collect(Collectors.groupingBy(existingInDatabase::contains, Collectors.mapping(a -> a, Collectors.toSet())));

        List<ApartmentRent> apartmentsForRentNew = saveNewRentApartments(apartmentsByExisting.get(Boolean.FALSE));

        List<ApartmentRent> apartmentsForRentUpdated = updateExistingRentApartments(apartmentsByExisting.get(Boolean.TRUE));

        return Collections.emptyList(); //TODO return total number of affected apartments (or apartment objects?
    }

    public List<ApartmentRent> saveNewRentApartments(Set<ApartmentRent> apartments) {

        if (Objects.isNull(apartments) || apartments.isEmpty()) {

            LOGGER.info("[ApartmentSaveService] There are no new rent apartments to save");
            return Collections.emptyList();
        }

        apartments = updateLocation(apartments);

        LOGGER.info("Save new apartments for rent. Found: {}", apartments.size());

        return apartmentRentRepository.save(apartments);
    }

    public List<ApartmentRent> updateExistingRentApartments(Set<ApartmentRent> apartments) {

        if (Objects.isNull(apartments) || apartments.isEmpty()) {

            LOGGER.info("[ApartmentSaveService] There are no rent apartments to update");
            return Collections.emptyList();
        }

        LOGGER.info("Update existing rent apartments. Found: {}", apartments.size());

        Map<Long, ApartmentRent> apartmentByApartmentIdExisting = apartmentRentRepository.findByApartmentIdIn(apartments.stream()
            .map(Apartment::getApartmentId)
            .collect(Collectors.toSet())).stream()
            .collect(Collectors.toMap(ApartmentRent::getApartmentId, a -> a));

        Set<ApartmentRent> apartmentsToUpdate = new HashSet<>();

        for (ApartmentRent apartmentRent : apartments) {

            ApartmentRent existingApartment = apartmentByApartmentIdExisting.get(apartmentRent.getApartmentId());

            if (!existingApartment.getPrices().containsAll(apartmentRent.getPrices())) {

                Set<Price> pricesNew = apartmentRent.getPrices();

                ZonedDateTime updateDate = apartmentRent.getUpdated();

                for (Price price : pricesNew) {

                    price.setApartment(existingApartment);
                    price.setUpdated(updateDate);
                }

                existingApartment.setUpdated(updateDate);

                existingApartment.setType(apartmentRent.getType());
                existingApartment.setOwner(apartmentRent.getOwner());
                existingApartment.setLocation(apartmentRent.getLocation());

                existingApartment.getPrices().addAll(apartmentRent.getPrices());

                apartmentsToUpdate.add(existingApartment);
            }
        }

        List<ApartmentRent> updatedApartments = Collections.emptyList();

        if (!apartmentsToUpdate.isEmpty()) {

            apartmentsToUpdate = updateLocation(apartmentsToUpdate);

            updatedApartments = apartmentRentRepository.save(apartmentsToUpdate);
        }

        return updatedApartments;
    }

    public List<ApartmentRent> saveOrUpdateSaleApartments(Set<ApartmentSale> apartments) {

        Set<ApartmentSale> existingInDatabase = apartmentSaleRepository.findByApartmentIdIn(apartments.stream()
            .map(Apartment::getApartmentId)
            .collect(Collectors.toSet())
        );

        Map<Boolean, Set<ApartmentSale>> apartmentsByExisting = apartments.stream()
            .collect(Collectors.groupingBy(existingInDatabase::contains, Collectors.mapping(a -> a, Collectors.toSet())));

        List<ApartmentSale> apartmentsForSaleNew = saveNewSaleApartments(apartmentsByExisting.get(Boolean.FALSE));

        List<ApartmentSale> apartmentsForSaleUpdated = updateExistingSaleApartments(apartmentsByExisting.get(Boolean.TRUE));

        return Collections.emptyList(); //TODO return total number of affected apartments (or apartment objects?)
    }

    public List<ApartmentSale> saveNewSaleApartments(Set<ApartmentSale> apartments) {

        if (Objects.isNull(apartments) || apartments.isEmpty()) {

            LOGGER.info("[ApartmentSaveService] There are no new apartments to save");
            return Collections.emptyList();
        }

        LOGGER.info("Save new apartments for sale. Found: {}", apartments.size());

        return apartmentSaleRepository.save(apartments);
    }

    public List<ApartmentSale> updateExistingSaleApartments(Set<ApartmentSale> apartments) {

        if (Objects.isNull(apartments) || apartments.isEmpty()) {

            LOGGER.info("[ApartmentSaveService] There are no sale apartments to update");
            return Collections.emptyList();
        }

        LOGGER.info("Update existing sale apartments. Found: {}", apartments.size());

        //TODO

        return null;
    }

    public <T extends Apartment> Set<T> updateLocation(Set<T> apartments) {

        for (Apartment apartment : apartments) {

            if (apartment.getLocation() != null && apartment.getLocation().getAddress() != null) {

                Optional<Location> location = locationRepository.findByAddress(apartment.getLocation().getAddress());

                if (location.isPresent()) {

                    apartment.setLocation(location.get());
                } else {

                    Location saved = locationRepository.save(apartment.getLocation());
                    locationRepository.flush();

                    apartment.setLocation(saved);
                }
            }
        }

        return apartments;
    }
}
