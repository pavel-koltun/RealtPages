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
     * POST  /apartment-rents : Create a new apartmentRent.
     *
     * @param apartmentRent the apartmentRent to create
     * @return the ResponseEntity with status 201 (Created) and with body the new apartmentRent, or with status 400 (Bad Request) if the apartmentRent has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/apartment-rents",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ApartmentRent> createApartmentRent(@Valid @RequestBody ApartmentRent apartmentRent) throws URISyntaxException {
        log.debug("REST request to save ApartmentRent : {}", apartmentRent);
        if (apartmentRent.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("apartmentRent", "idexists", "A new apartmentRent cannot already have an ID")).body(null);
        }
        ApartmentRent result = apartmentRentService.save(apartmentRent);
        return ResponseEntity.created(new URI("/api/apartment-rents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("apartmentRent", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /apartment-rents : Updates an existing apartmentRent.
     *
     * @param apartmentRent the apartmentRent to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated apartmentRent,
     * or with status 400 (Bad Request) if the apartmentRent is not valid,
     * or with status 500 (Internal Server Error) if the apartmentRent couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/apartment-rents",
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
     * GET  /apartment-rents : get all the apartmentRents.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of apartmentRents in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/apartment-rents",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ApartmentRent>> getAllApartmentRents(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ApartmentRents");
        Page<ApartmentRent> page = apartmentRentService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/apartment-rents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /apartment-rents/:id : get the "id" apartmentRent.
     *
     * @param id the id of the apartmentRent to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the apartmentRent, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/apartment-rents/{id}",
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
     * DELETE  /apartment-rents/:id : delete the "id" apartmentRent.
     *
     * @param id the id of the apartmentRent to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/apartment-rents/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteApartmentRent(@PathVariable Long id) {
        log.debug("REST request to delete ApartmentRent : {}", id);
        apartmentRentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("apartmentRent", id.toString())).build();
    }

    /**
     * SEARCH  /_search/apartment-rents?query=:query : search for the apartmentRent corresponding
     * to the query.
     *
     * @param query the query of the apartmentRent search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/apartment-rents",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ApartmentRent>> searchApartmentRents(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of ApartmentRents for query {}", query);
        Page<ApartmentRent> page = apartmentRentService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/apartment-rents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
