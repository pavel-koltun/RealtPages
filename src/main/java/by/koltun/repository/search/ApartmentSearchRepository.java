package by.koltun.repository.search;

import by.koltun.domain.Apartment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Apartment entity.
 */
public interface ApartmentSearchRepository extends ElasticsearchRepository<Apartment, Long> {
}
