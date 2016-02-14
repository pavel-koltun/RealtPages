package by.koltun.repository;

import by.koltun.domain.ApartmentRent;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ApartmentRent entity.
 */
public interface ApartmentRentRepository extends JpaRepository<ApartmentRent,Long> {

}
