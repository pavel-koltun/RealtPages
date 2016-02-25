package by.koltun.repository.search;

import by.koltun.domain.ApartmentSale;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ApartmentSale entity.
 */
public interface ApartmentSaleSearchRepository extends ElasticsearchRepository<ApartmentSale, Long> {
}
