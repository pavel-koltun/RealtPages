package by.koltun.web.rest;

import com.codahale.metrics.annotation.Timed;
import by.koltun.domain.ApartmentSale;
import by.koltun.service.ApartmentSaleService;
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
 * REST controller for managing ApartmentSale.
 */
@RestController
@RequestMapping("/api")
public class ApartmentSaleResource {

    private final Logger log = LoggerFactory.getLogger(ApartmentSaleResource.class);
        
    @Inject
    private ApartmentSaleService apartmentSaleService;
    
    /**
     * POST  /apartment-sales : Create a new apartmentSale.
     *
     * @param apartmentSale the apartmentSale to create
     * @return the ResponseEntity with status 201 (Created) and with body the new apartmentSale, or with status 400 (Bad Request) if the apartmentSale has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/apartment-sales",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ApartmentSale> createApartmentSale(@Valid @RequestBody ApartmentSale apartmentSale) throws URISyntaxException {
        log.debug("REST request to save ApartmentSale : {}", apartmentSale);
        if (apartmentSale.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("apartmentSale", "idexists", "A new apartmentSale cannot already have an ID")).body(null);
        }
        ApartmentSale result = apartmentSaleService.save(apartmentSale);
        return ResponseEntity.created(new URI("/api/apartment-sales/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("apartmentSale", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /apartment-sales : Updates an existing apartmentSale.
     *
     * @param apartmentSale the apartmentSale to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated apartmentSale,
     * or with status 400 (Bad Request) if the apartmentSale is not valid,
     * or with status 500 (Internal Server Error) if the apartmentSale couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/apartment-sales",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ApartmentSale> updateApartmentSale(@Valid @RequestBody ApartmentSale apartmentSale) throws URISyntaxException {
        log.debug("REST request to update ApartmentSale : {}", apartmentSale);
        if (apartmentSale.getId() == null) {
            return createApartmentSale(apartmentSale);
        }
        ApartmentSale result = apartmentSaleService.save(apartmentSale);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("apartmentSale", apartmentSale.getId().toString()))
            .body(result);
    }

    /**
     * GET  /apartment-sales : get all the apartmentSales.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of apartmentSales in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/apartment-sales",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ApartmentSale>> getAllApartmentSales(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ApartmentSales");
        Page<ApartmentSale> page = apartmentSaleService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/apartment-sales");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /apartment-sales/:id : get the "id" apartmentSale.
     *
     * @param id the id of the apartmentSale to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the apartmentSale, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/apartment-sales/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ApartmentSale> getApartmentSale(@PathVariable Long id) {
        log.debug("REST request to get ApartmentSale : {}", id);
        ApartmentSale apartmentSale = apartmentSaleService.findOne(id);
        return Optional.ofNullable(apartmentSale)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /apartment-sales/:id : delete the "id" apartmentSale.
     *
     * @param id the id of the apartmentSale to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/apartment-sales/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteApartmentSale(@PathVariable Long id) {
        log.debug("REST request to delete ApartmentSale : {}", id);
        apartmentSaleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("apartmentSale", id.toString())).build();
    }

    /**
     * SEARCH  /_search/apartment-sales?query=:query : search for the apartmentSale corresponding
     * to the query.
     *
     * @param query the query of the apartmentSale search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/apartment-sales",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ApartmentSale>> searchApartmentSales(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of ApartmentSales for query {}", query);
        Page<ApartmentSale> page = apartmentSaleService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/apartment-sales");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
