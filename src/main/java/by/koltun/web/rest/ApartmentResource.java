package by.koltun.web.rest;

import com.codahale.metrics.annotation.Timed;
import by.koltun.domain.Apartment;
import by.koltun.repository.ApartmentRepository;
import by.koltun.repository.search.ApartmentSearchRepository;
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
 * REST controller for managing Apartment.
 */
@RestController
@RequestMapping("/api")
public class ApartmentResource {

    private final Logger log = LoggerFactory.getLogger(ApartmentResource.class);
        
    @Inject
    private ApartmentRepository apartmentRepository;
    
    @Inject
    private ApartmentSearchRepository apartmentSearchRepository;
    
    /**
     * POST  /apartments : Create a new apartment.
     *
     * @param apartment the apartment to create
     * @return the ResponseEntity with status 201 (Created) and with body the new apartment, or with status 400 (Bad Request) if the apartment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/apartments",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Apartment> createApartment(@Valid @RequestBody Apartment apartment) throws URISyntaxException {
        log.debug("REST request to save Apartment : {}", apartment);
        if (apartment.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("apartment", "idexists", "A new apartment cannot already have an ID")).body(null);
        }
        Apartment result = apartmentRepository.save(apartment);
        apartmentSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/apartments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("apartment", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /apartments : Updates an existing apartment.
     *
     * @param apartment the apartment to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated apartment,
     * or with status 400 (Bad Request) if the apartment is not valid,
     * or with status 500 (Internal Server Error) if the apartment couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/apartments",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Apartment> updateApartment(@Valid @RequestBody Apartment apartment) throws URISyntaxException {
        log.debug("REST request to update Apartment : {}", apartment);
        if (apartment.getId() == null) {
            return createApartment(apartment);
        }
        Apartment result = apartmentRepository.save(apartment);
        apartmentSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("apartment", apartment.getId().toString()))
            .body(result);
    }

    /**
     * GET  /apartments : get all the apartments.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of apartments in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/apartments",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Apartment>> getAllApartments(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Apartments");
        Page<Apartment> page = apartmentRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/apartments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /apartments/:id : get the "id" apartment.
     *
     * @param id the id of the apartment to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the apartment, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/apartments/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Apartment> getApartment(@PathVariable Long id) {
        log.debug("REST request to get Apartment : {}", id);
        Apartment apartment = apartmentRepository.findOne(id);
        return Optional.ofNullable(apartment)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /apartments/:id : delete the "id" apartment.
     *
     * @param id the id of the apartment to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/apartments/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteApartment(@PathVariable Long id) {
        log.debug("REST request to delete Apartment : {}", id);
        apartmentRepository.delete(id);
        apartmentSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("apartment", id.toString())).build();
    }

    /**
     * SEARCH  /_search/apartments?query=:query : search for the apartment corresponding
     * to the query.
     *
     * @param query the query of the apartment search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/apartments",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Apartment>> searchApartments(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Apartments for query {}", query);
        Page<Apartment> page = apartmentSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/apartments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
