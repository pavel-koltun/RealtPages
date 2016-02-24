package by.koltun.service;

import by.koltun.domain.ApartmentRent;
import by.koltun.repository.ApartmentRentRepository;
import by.koltun.repository.search.ApartmentRentSearchRepository;
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
 * Service Implementation for managing ApartmentRent.
 */
@Service
@Transactional
public class ApartmentRentService {

    private final Logger log = LoggerFactory.getLogger(ApartmentRentService.class);
    
    @Inject
    private ApartmentRentRepository apartmentRentRepository;
    
    @Inject
    private ApartmentRentSearchRepository apartmentRentSearchRepository;
    
    /**
     * Save a apartmentRent.
     * @return the persisted entity
     */
    public ApartmentRent save(ApartmentRent apartmentRent) {
        log.debug("Request to save ApartmentRent : {}", apartmentRent);
        ApartmentRent result = apartmentRentRepository.save(apartmentRent);
        apartmentRentSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the apartmentRents.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ApartmentRent> findAll(Pageable pageable) {
        log.debug("Request to get all ApartmentRents");
        Page<ApartmentRent> result = apartmentRentRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one apartmentRent by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ApartmentRent findOne(Long id) {
        log.debug("Request to get ApartmentRent : {}", id);
        ApartmentRent apartmentRent = apartmentRentRepository.findOne(id);
        return apartmentRent;
    }

    /**
     *  delete the  apartmentRent by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete ApartmentRent : {}", id);
        apartmentRentRepository.delete(id);
        apartmentRentSearchRepository.delete(id);
    }

    /**
     * search for the apartmentRent corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<ApartmentRent> search(String query) {
        
        log.debug("REST request to search ApartmentRents for query {}", query);
        return StreamSupport
            .stream(apartmentRentSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
