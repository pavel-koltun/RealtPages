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
     * 
     * @param apartmentSale the entity to save
     * @return the persisted entity
     */
    public ApartmentSale save(ApartmentSale apartmentSale) {
        log.debug("Request to save ApartmentSale : {}", apartmentSale);
        ApartmentSale result = apartmentSaleRepository.save(apartmentSale);
        apartmentSaleSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the apartmentSales.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ApartmentSale> findAll(Pageable pageable) {
        log.debug("Request to get all ApartmentSales");
        Page<ApartmentSale> result = apartmentSaleRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one apartmentSale by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ApartmentSale findOne(Long id) {
        log.debug("Request to get ApartmentSale : {}", id);
        ApartmentSale apartmentSale = apartmentSaleRepository.findOne(id);
        return apartmentSale;
    }

    /**
     *  Delete the  apartmentSale by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ApartmentSale : {}", id);
        apartmentSaleRepository.delete(id);
        apartmentSaleSearchRepository.delete(id);
    }

    /**
     * Search for the apartmentSale corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ApartmentSale> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ApartmentSales for query {}", query);
        return apartmentSaleSearchRepository.search(queryStringQuery(query), pageable);
    }
}
