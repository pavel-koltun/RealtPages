package by.koltun.repository;

import by.koltun.domain.Apartment;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Apartment entity.
 */
public interface ApartmentRepository extends JpaRepository<Apartment,Long> {

}
