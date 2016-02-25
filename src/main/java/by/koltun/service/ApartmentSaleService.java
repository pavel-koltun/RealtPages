package by.koltun.service;

import by.koltun.domain.ApartmentSale;
import by.koltun.repository.ApartmentSaleRepository;
import by.koltun.repository.search.ApartmentSaleSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ApartmentSale.
 */
@Service
@Transactional
public class ApartmentSaleService {

    private final Logger log = LoggerFactory.getLogger(ApartmentSaleService.class);
    
    @Inject
    private ApartmentSaleRepository apartmentSaleRepository;
    
    @Inject
    private ApartmentSaleSearchRepository apartmentSaleSearchRepository;
    
    /**
     * Save a apartmentSale.
     * @return the persisted entity
     */
    public ApartmentSale save(ApartmentSale apartmentSale) {
        log.debug("Request to save ApartmentSale : {}", apartmentSale);
        ApartmentSale result = apartmentSaleRepository.save(apartmentSale);
        apartmentSaleSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the apartmentSales.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ApartmentSale> findAll(Pageable pageable) {
        log.debug("Request to get all ApartmentSales");
        Page<ApartmentSale> result = apartmentSaleRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one apartmentSale by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ApartmentSale findOne(Long id) {
        log.debug("Request to get ApartmentSale : {}", id);
        ApartmentSale apartmentSale = apartmentSaleRepository.findOne(id);
        return apartmentSale;
    }

    /**
     *  delete the  apartmentSale by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete ApartmentSale : {}", id);
        apartmentSaleRepository.delete(id);
        apartmentSaleSearchRepository.delete(id);
    }

    /**
     * search for the apartmentSale corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<ApartmentSale> search(String query) {
        
        log.debug("REST request to search ApartmentSales for query {}", query);
        return StreamSupport
            .stream(apartmentSaleSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
