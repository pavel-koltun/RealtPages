package by.koltun.repository.search;

import by.koltun.domain.ApartmentRent;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ApartmentRent entity.
 */
public interface ApartmentRentSearchRepository extends ElasticsearchRepository<ApartmentRent, Long> {
}
