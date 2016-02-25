package by.koltun.repository;

import by.koltun.domain.ApartmentSale;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ApartmentSale entity.
 */
public interface ApartmentSaleRepository extends JpaRepository<ApartmentSale,Long> {

}
