package by.koltun.repository.search;

import by.koltun.domain.Price;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Price entity.
 */
public interface PriceSearchRepository extends ElasticsearchRepository<Price, Long> {
}
