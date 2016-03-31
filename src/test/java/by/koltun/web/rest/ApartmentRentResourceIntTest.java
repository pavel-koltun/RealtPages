package by.koltun.web.rest;

import by.koltun.OnlinerRealtPagesApp;
import by.koltun.domain.ApartmentRent;
import by.koltun.repository.ApartmentRentRepository;
import by.koltun.service.ApartmentRentService;
import by.koltun.repository.search.ApartmentRentSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import by.koltun.domain.enumeration.RentType;

/**
 * Test class for the ApartmentRentResource REST controller.
 *
 * @see ApartmentRentResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = OnlinerRealtPagesApp.class)
@WebAppConfiguration
@IntegrationTest
public class ApartmentRentResourceIntTest {


    private static final RentType DEFAULT_TYPE = RentType.ROOM;
    private static final RentType UPDATED_TYPE = RentType.ONE_ROOM;

    private static final Boolean DEFAULT_OWNER = false;
    private static final Boolean UPDATED_OWNER = true;

    @Inject
    private ApartmentRentRepository apartmentRentRepository;

    @Inject
    private ApartmentRentService apartmentRentService;

    @Inject
    private ApartmentRentSearchRepository apartmentRentSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restApartmentRentMockMvc;

    private ApartmentRent apartmentRent;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ApartmentRentResource apartmentRentResource = new ApartmentRentResource();
        ReflectionTestUtils.setField(apartmentRentResource, "apartmentRentService", apartmentRentService);
        this.restApartmentRentMockMvc = MockMvcBuilders.standaloneSetup(apartmentRentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        apartmentRentSearchRepository.deleteAll();
        apartmentRent = new ApartmentRent();
        apartmentRent.setType(DEFAULT_TYPE);
        apartmentRent.setOwner(DEFAULT_OWNER);
    }

    @Test
    @Transactional
    public void createApartmentRent() throws Exception {
        int databaseSizeBeforeCreate = apartmentRentRepository.findAll().size();

        // Create the ApartmentRent

        restApartmentRentMockMvc.perform(post("/api/apartment-rents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(apartmentRent)))
                .andExpect(status().isCreated());

        // Validate the ApartmentRent in the database
        List<ApartmentRent> apartmentRents = apartmentRentRepository.findAll();
        assertThat(apartmentRents).hasSize(databaseSizeBeforeCreate + 1);
        ApartmentRent testApartmentRent = apartmentRents.get(apartmentRents.size() - 1);
        assertThat(testApartmentRent.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testApartmentRent.getOwner()).isEqualTo(DEFAULT_OWNER);

        // Validate the ApartmentRent in ElasticSearch
        ApartmentRent apartmentRentEs = apartmentRentSearchRepository.findOne(testApartmentRent.getId());
        assertThat(apartmentRentEs).isEqualToComparingFieldByField(testApartmentRent);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = apartmentRentRepository.findAll().size();
        // set the field null
        apartmentRent.setType(null);

        // Create the ApartmentRent, which fails.

        restApartmentRentMockMvc.perform(post("/api/apartment-rents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(apartmentRent)))
                .andExpect(status().isBadRequest());

        List<ApartmentRent> apartmentRents = apartmentRentRepository.findAll();
        assertThat(apartmentRents).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOwnerIsRequired() throws Exception {
        int databaseSizeBeforeTest = apartmentRentRepository.findAll().size();
        // set the field null
        apartmentRent.setOwner(null);

        // Create the ApartmentRent, which fails.

        restApartmentRentMockMvc.perform(post("/api/apartment-rents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(apartmentRent)))
                .andExpect(status().isBadRequest());

        List<ApartmentRent> apartmentRents = apartmentRentRepository.findAll();
        assertThat(apartmentRents).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllApartmentRents() throws Exception {
        // Initialize the database
        apartmentRentRepository.saveAndFlush(apartmentRent);

        // Get all the apartmentRents
        restApartmentRentMockMvc.perform(get("/api/apartment-rents?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(apartmentRent.getId().intValue())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER.booleanValue())));
    }

    @Test
    @Transactional
    public void getApartmentRent() throws Exception {
        // Initialize the database
        apartmentRentRepository.saveAndFlush(apartmentRent);

        // Get the apartmentRent
        restApartmentRentMockMvc.perform(get("/api/apartment-rents/{id}", apartmentRent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(apartmentRent.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.owner").value(DEFAULT_OWNER.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingApartmentRent() throws Exception {
        // Get the apartmentRent
        restApartmentRentMockMvc.perform(get("/api/apartment-rents/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApartmentRent() throws Exception {
        // Initialize the database
        apartmentRentService.save(apartmentRent);

        int databaseSizeBeforeUpdate = apartmentRentRepository.findAll().size();

        // Update the apartmentRent
        ApartmentRent updatedApartmentRent = new ApartmentRent();
        updatedApartmentRent.setId(apartmentRent.getId());
        updatedApartmentRent.setType(UPDATED_TYPE);
        updatedApartmentRent.setOwner(UPDATED_OWNER);

        restApartmentRentMockMvc.perform(put("/api/apartment-rents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedApartmentRent)))
                .andExpect(status().isOk());

        // Validate the ApartmentRent in the database
        List<ApartmentRent> apartmentRents = apartmentRentRepository.findAll();
        assertThat(apartmentRents).hasSize(databaseSizeBeforeUpdate);
        ApartmentRent testApartmentRent = apartmentRents.get(apartmentRents.size() - 1);
        assertThat(testApartmentRent.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testApartmentRent.getOwner()).isEqualTo(UPDATED_OWNER);

        // Validate the ApartmentRent in ElasticSearch
        ApartmentRent apartmentRentEs = apartmentRentSearchRepository.findOne(testApartmentRent.getId());
        assertThat(apartmentRentEs).isEqualToComparingFieldByField(testApartmentRent);
    }

    @Test
    @Transactional
    public void deleteApartmentRent() throws Exception {
        // Initialize the database
        apartmentRentService.save(apartmentRent);

        int databaseSizeBeforeDelete = apartmentRentRepository.findAll().size();

        // Get the apartmentRent
        restApartmentRentMockMvc.perform(delete("/api/apartment-rents/{id}", apartmentRent.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean apartmentRentExistsInEs = apartmentRentSearchRepository.exists(apartmentRent.getId());
        assertThat(apartmentRentExistsInEs).isFalse();

        // Validate the database is empty
        List<ApartmentRent> apartmentRents = apartmentRentRepository.findAll();
        assertThat(apartmentRents).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchApartmentRent() throws Exception {
        // Initialize the database
        apartmentRentService.save(apartmentRent);

        // Search the apartmentRent
        restApartmentRentMockMvc.perform(get("/api/_search/apartment-rents?query=id:" + apartmentRent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(apartmentRent.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER.booleanValue())));
    }
}
