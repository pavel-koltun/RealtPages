package by.koltun.web.rest;

import com.codahale.metrics.annotation.Timed;
import by.koltun.domain.ApartmentRent;
import by.koltun.service.ApartmentRentService;
import by.koltun.web.rest.util.HeaderUtil;
import by.koltun.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ApartmentRent.
 */
@RestController
@RequestMapping("/api")
public class ApartmentRentResource {

    private final Logger log = LoggerFactory.getLogger(ApartmentRentResource.class);
        
    @Inject
    private ApartmentRentService apartmentRentService;
    
    /**
     * POST  /apartmentRents -> Create a new apartmentRent.
     */
    @RequestMapping(value = "/apartmentRents",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ApartmentRent> createApartmentRent(@Valid @RequestBody ApartmentRent apartmentRent) throws URISyntaxException {
        log.debug("REST request to save ApartmentRent : {}", apartmentRent);
        if (apartmentRent.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("apartmentRent", "idexists", "A new apartmentRent cannot already have an ID")).body(null);
        }
        ApartmentRent result = apartmentRentService.save(apartmentRent);
        return ResponseEntity.created(new URI("/api/apartmentRents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("apartmentRent", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /apartmentRents -> Updates an existing apartmentRent.
     */
    @RequestMapping(value = "/apartmentRents",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ApartmentRent> updateApartmentRent(@Valid @RequestBody ApartmentRent apartmentRent) throws URISyntaxException {
        log.debug("REST request to update ApartmentRent : {}", apartmentRent);
        if (apartmentRent.getId() == null) {
            return createApartmentRent(apartmentRent);
        }
        ApartmentRent result = apartmentRentService.save(apartmentRent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("apartmentRent", apartmentRent.getId().toString()))
            .body(result);
    }

    /**
     * GET  /apartmentRents -> get all the apartmentRents.
     */
    @RequestMapping(value = "/apartmentRents",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ApartmentRent>> getAllApartmentRents(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ApartmentRents");
        Page<ApartmentRent> page = apartmentRentService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/apartmentRents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /apartmentRents/:id -> get the "id" apartmentRent.
     */
    @RequestMapping(value = "/apartmentRents/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ApartmentRent> getApartmentRent(@PathVariable Long id) {
        log.debug("REST request to get ApartmentRent : {}", id);
        ApartmentRent apartmentRent = apartmentRentService.findOne(id);
        return Optional.ofNullable(apartmentRent)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /apartmentRents/:id -> delete the "id" apartmentRent.
     */
    @RequestMapping(value = "/apartmentRents/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteApartmentRent(@PathVariable Long id) {
        log.debug("REST request to delete ApartmentRent : {}", id);
        apartmentRentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("apartmentRent", id.toString())).build();
    }

    /**
     * SEARCH  /_search/apartmentRents/:query -> search for the apartmentRent corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/apartmentRents/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ApartmentRent> searchApartmentRents(@PathVariable String query) {
        log.debug("Request to search ApartmentRents for query {}", query);
        return apartmentRentService.search(query);
    }
}
