package by.koltun.service;

import by.koltun.domain.Location;
import by.koltun.repository.LocationRepository;
import by.koltun.repository.search.LocationSearchRepository;
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
 * Service Implementation for managing Location.
 */
@Service
@Transactional
public class LocationService {

    private final Logger log = LoggerFactory.getLogger(LocationService.class);
    
    @Inject
    private LocationRepository locationRepository;
    
    @Inject
    private LocationSearchRepository locationSearchRepository;
    
    /**
     * Save a location.
     * @return the persisted entity
     */
    public Location save(Location location) {
        log.debug("Request to save Location : {}", location);
        Location result = locationRepository.save(location);
        locationSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the locations.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Location> findAll(Pageable pageable) {
        log.debug("Request to get all Locations");
        Page<Location> result = locationRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one location by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Location findOne(Long id) {
        log.debug("Request to get Location : {}", id);
        Location location = locationRepository.findOne(id);
        return location;
    }

    /**
     *  delete the  location by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Location : {}", id);
        locationRepository.delete(id);
        locationSearchRepository.delete(id);
    }

    /**
     * search for the location corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<Location> search(String query) {
        
        log.debug("REST request to search Locations for query {}", query);
        return StreamSupport
            .stream(locationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
