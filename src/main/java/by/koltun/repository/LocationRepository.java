package by.koltun.repository;

import by.koltun.domain.Location;

import org.springframework.data.jpa.repository.*;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Location entity.
 */
public interface LocationRepository extends JpaRepository<Location,Long> {

    Optional<Location> findByAddress(String address);
}
