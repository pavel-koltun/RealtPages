package by.koltun.repository;

import by.koltun.domain.ApartmentRent;

import org.springframework.data.jpa.repository.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Spring Data JPA repository for the ApartmentRent entity.
 */
public interface ApartmentRentRepository extends JpaRepository<ApartmentRent,Long> {

    /**
     * Поиск объектов аренды по идентификаторам из внешней системы
     * @param apartmentIds набор идентификаторов из внешней системы
     * @return Коллекция объектов аренды
     */
    Set<ApartmentRent> findByApartmentIdIn(Iterable<Long> apartmentIds);

    Optional<ApartmentRent> findByApartmentId(Long apartmentId);

    Optional<ApartmentRent> findTopByOrderByUpdatedDesc();
}
