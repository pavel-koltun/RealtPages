package by.koltun.web.rest;

import com.codahale.metrics.annotation.Timed;
import by.koltun.domain.Price;
import by.koltun.repository.PriceRepository;
import by.koltun.repository.search.PriceSearchRepository;
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
 * REST controller for managing Price.
 */
@RestController
@RequestMapping("/api")
public class PriceResource {

    private final Logger log = LoggerFactory.getLogger(PriceResource.class);
        
    @Inject
    private PriceRepository priceRepository;
    
    @Inject
    private PriceSearchRepository priceSearchRepository;
    
    /**
     * POST  /prices : Create a new price.
     *
     * @param price the price to create
     * @return the ResponseEntity with status 201 (Created) and with body the new price, or with status 400 (Bad Request) if the price has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/prices",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Price> createPrice(@Valid @RequestBody Price price) throws URISyntaxException {
        log.debug("REST request to save Price : {}", price);
        if (price.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("price", "idexists", "A new price cannot already have an ID")).body(null);
        }
        Price result = priceRepository.save(price);
        priceSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/prices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("price", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /prices : Updates an existing price.
     *
     * @param price the price to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated price,
     * or with status 400 (Bad Request) if the price is not valid,
     * or with status 500 (Internal Server Error) if the price couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/prices",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Price> updatePrice(@Valid @RequestBody Price price) throws URISyntaxException {
        log.debug("REST request to update Price : {}", price);
        if (price.getId() == null) {
            return createPrice(price);
        }
        Price result = priceRepository.save(price);
        priceSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("price", price.getId().toString()))
            .body(result);
    }

    /**
     * GET  /prices : get all the prices.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of prices in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/prices",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Price>> getAllPrices(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Prices");
        Page<Price> page = priceRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/prices");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /prices/:id : get the "id" price.
     *
     * @param id the id of the price to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the price, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/prices/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Price> getPrice(@PathVariable Long id) {
        log.debug("REST request to get Price : {}", id);
        Price price = priceRepository.findOne(id);
        return Optional.ofNullable(price)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /prices/:id : delete the "id" price.
     *
     * @param id the id of the price to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/prices/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePrice(@PathVariable Long id) {
        log.debug("REST request to delete Price : {}", id);
        priceRepository.delete(id);
        priceSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("price", id.toString())).build();
    }

    /**
     * SEARCH  /_search/prices?query=:query : search for the price corresponding
     * to the query.
     *
     * @param query the query of the price search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/prices",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Price>> searchPrices(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Prices for query {}", query);
        Page<Price> page = priceSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/prices");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
