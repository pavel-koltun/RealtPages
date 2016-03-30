package by.koltun.repository;

import by.koltun.domain.ApartmentSale;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the ApartmentSale entity.
 */
public interface ApartmentSaleRepository extends JpaRepository<ApartmentSale,Long> {

    /**
     * Search apartments by {@link by.koltun.domain.Apartment#apartmentId} property
     * @param apartmentIds list of apartmentIds
     * @return found apartments list
     */
    Set<ApartmentSale> findByApartmentIdIn(Iterable<Long> apartmentIds);
}
