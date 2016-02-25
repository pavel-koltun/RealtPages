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
     * POST  /apartmentSales -> Create a new apartmentSale.
     */
    @RequestMapping(value = "/apartmentSales",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ApartmentSale> createApartmentSale(@Valid @RequestBody ApartmentSale apartmentSale) throws URISyntaxException {
        log.debug("REST request to save ApartmentSale : {}", apartmentSale);
        if (apartmentSale.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("apartmentSale", "idexists", "A new apartmentSale cannot already have an ID")).body(null);
        }
        ApartmentSale result = apartmentSaleService.save(apartmentSale);
        return ResponseEntity.created(new URI("/api/apartmentSales/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("apartmentSale", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /apartmentSales -> Updates an existing apartmentSale.
     */
    @RequestMapping(value = "/apartmentSales",
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
     * GET  /apartmentSales -> get all the apartmentSales.
     */
    @RequestMapping(value = "/apartmentSales",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ApartmentSale>> getAllApartmentSales(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ApartmentSales");
        Page<ApartmentSale> page = apartmentSaleService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/apartmentSales");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /apartmentSales/:id -> get the "id" apartmentSale.
     */
    @RequestMapping(value = "/apartmentSales/{id}",
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
     * DELETE  /apartmentSales/:id -> delete the "id" apartmentSale.
     */
    @RequestMapping(value = "/apartmentSales/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteApartmentSale(@PathVariable Long id) {
        log.debug("REST request to delete ApartmentSale : {}", id);
        apartmentSaleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("apartmentSale", id.toString())).build();
    }

    /**
     * SEARCH  /_search/apartmentSales/:query -> search for the apartmentSale corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/apartmentSales/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ApartmentSale> searchApartmentSales(@PathVariable String query) {
        log.debug("Request to search ApartmentSales for query {}", query);
        return apartmentSaleService.search(query);
    }
}
