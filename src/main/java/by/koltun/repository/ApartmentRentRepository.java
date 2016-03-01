package by.koltun.repository;

import by.koltun.domain.ApartmentRent;

import org.springframework.data.jpa.repository.*;

import java.util.Optional;
import java.util.Set;

/**
 * Spring Data JPA repository for the ApartmentRent entity.
 */
public interface ApartmentRentRepository extends JpaRepository<ApartmentRent,Long> {

    Set<ApartmentRent> findByApartmentIdIn(Iterable<Long> apartmentIds);

    Optional<ApartmentRent> findByApartmentId(Long apartmentId);

    Optional<ApartmentRent> findTopByOrderByUpdatedDesc();
}
