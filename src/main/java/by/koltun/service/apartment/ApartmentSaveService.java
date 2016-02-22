package by.koltun.service.apartment;

import by.koltun.domain.ApartmentRent;
import by.koltun.domain.Location;
import by.koltun.domain.Price;
import by.koltun.domain.to.Apartment;
import by.koltun.repository.ApartmentRentRepository;
import by.koltun.repository.LocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by koltun on 2/14/16.
 */
@Service
public class ApartmentSaveService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApartmentSaveService.class);

    @Autowired ApartmentRentRepository apartmentRentRepository;
    @Autowired LocationRepository locationRepository;

    public void saveRentApartments(Set<ApartmentRent> apartments) {

        Set<Long> apartmentIds = apartments.stream()
            .map(ApartmentRent::getApartmentId)
            .collect(Collectors.toSet());

        Set<ApartmentRent> existingApartments = apartmentRentRepository.findByApartmentIdIn(apartmentIds);

        Set<ApartmentRent> newApartments = apartments.stream()
            .filter(((Predicate<ApartmentRent>) existingApartments::contains).negate())
            .collect(Collectors.toSet());

        Set<ApartmentRent> existedAparments = apartments.stream()
            .filter(existingApartments::contains)
            .collect(Collectors.toSet());

        LOGGER.info("Saving {} new rent apartments.", newApartments.size());
        apartmentRentRepository.save(newApartments);

        updateExistingApartmentsIfPriceChanged(existedAparments);
    }

    public void updateExistingApartmentsIfPriceChanged(Set<ApartmentRent> updateApartments) {

        Set<ApartmentRent> existingApartments = apartmentRentRepository.findByApartmentIdIn(
            updateApartments.stream()
                .map(ApartmentRent::getApartmentId)
                .collect(Collectors.toSet()));

        Map<Long, Set<Price>> priceByExistingApartments = existingApartments.stream()
            .collect(Collectors.toMap(ApartmentRent::getApartmentId, ApartmentRent::getPrices));

        Map<Long, Set<Price>> priceByUpdateApartments = updateApartments.stream()
            .collect(Collectors.toMap(ApartmentRent::getApartmentId, ApartmentRent::getPrices));

        Set<ApartmentRent> apartmentsToUpdateWithNewPrices = new HashSet<>();

        for (Map.Entry<Long, Set<Price>> entry : priceByUpdateApartments.entrySet()) {

            Set<Price> prices = priceByExistingApartments.get(entry.getKey());
            Set<Price> updatedPrices = entry.getValue().stream()
                .peek(p -> p.setPriceUsd(p.getPriceUsd().setScale(2)))
                .collect(Collectors.toSet());

            if (prices != null && !prices.containsAll(updatedPrices)) {

                prices.addAll(updatedPrices);

                Optional<ApartmentRent> apartmentRent = apartmentRentRepository.findByApartmentId(entry.getKey());

                if (apartmentRent.isPresent()) {

                    ApartmentRent a = apartmentRent.get();

                    a.getPrices().addAll(prices);

                    a.getPrices().stream()
                        .filter(p -> p.getApartment().getId() == null)
                        .forEach(p -> p.setApartment(a));

                    LOGGER.info("Update apartment {}, added prices count {}", a.getUrl(), updatedPrices.size());

                    apartmentsToUpdateWithNewPrices.add(a);
                }
            }
        }

        if (!apartmentsToUpdateWithNewPrices.isEmpty()) {

            apartmentRentRepository.save(apartmentsToUpdateWithNewPrices);
        }
    }
}
