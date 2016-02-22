package by.koltun.web.rest;

import by.koltun.Application;
import by.koltun.domain.Apartment;
import by.koltun.repository.ApartmentRepository;
import by.koltun.repository.search.ApartmentSearchRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ApartmentResource REST controller.
 *
 * @see ApartmentResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ApartmentResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));


    private static final Long DEFAULT_APARTMENT_ID = 1L;
    private static final Long UPDATED_APARTMENT_ID = 2L;

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATED_STR = dateTimeFormatter.format(DEFAULT_CREATED);

    private static final ZonedDateTime DEFAULT_UPDATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_UPDATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_UPDATED_STR = dateTimeFormatter.format(DEFAULT_UPDATED);
    private static final String DEFAULT_URL = "AAAAA";
    private static final String UPDATED_URL = "BBBBB";

    @Inject
    private ApartmentRepository apartmentRepository;

    @Inject
    private ApartmentSearchRepository apartmentSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restApartmentMockMvc;

    private Apartment apartment;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ApartmentResource apartmentResource = new ApartmentResource();
        ReflectionTestUtils.setField(apartmentResource, "apartmentSearchRepository", apartmentSearchRepository);
        ReflectionTestUtils.setField(apartmentResource, "apartmentRepository", apartmentRepository);
        this.restApartmentMockMvc = MockMvcBuilders.standaloneSetup(apartmentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        apartment = new Apartment();
        apartment.setApartmentId(DEFAULT_APARTMENT_ID);
        apartment.setCreated(DEFAULT_CREATED);
        apartment.setUpdated(DEFAULT_UPDATED);
        apartment.setUrl(DEFAULT_URL);
    }

    @Test
    @Transactional
    public void createApartment() throws Exception {
        int databaseSizeBeforeCreate = apartmentRepository.findAll().size();

        // Create the Apartment

        restApartmentMockMvc.perform(post("/api/apartments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(apartment)))
                .andExpect(status().isCreated());

        // Validate the Apartment in the database
        List<Apartment> apartments = apartmentRepository.findAll();
        assertThat(apartments).hasSize(databaseSizeBeforeCreate + 1);
        Apartment testApartment = apartments.get(apartments.size() - 1);
        assertThat(testApartment.getApartmentId()).isEqualTo(DEFAULT_APARTMENT_ID);
        assertThat(testApartment.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testApartment.getUpdated()).isEqualTo(DEFAULT_UPDATED);
        assertThat(testApartment.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    public void checkApartmentIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = apartmentRepository.findAll().size();
        // set the field null
        apartment.setApartmentId(null);

        // Create the Apartment, which fails.

        restApartmentMockMvc.perform(post("/api/apartments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(apartment)))
                .andExpect(status().isBadRequest());

        List<Apartment> apartments = apartmentRepository.findAll();
        assertThat(apartments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = apartmentRepository.findAll().size();
        // set the field null
        apartment.setCreated(null);

        // Create the Apartment, which fails.

        restApartmentMockMvc.perform(post("/api/apartments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(apartment)))
                .andExpect(status().isBadRequest());

        List<Apartment> apartments = apartmentRepository.findAll();
        assertThat(apartments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUpdatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = apartmentRepository.findAll().size();
        // set the field null
        apartment.setUpdated(null);

        // Create the Apartment, which fails.

        restApartmentMockMvc.perform(post("/api/apartments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(apartment)))
                .andExpect(status().isBadRequest());

        List<Apartment> apartments = apartmentRepository.findAll();
        assertThat(apartments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllApartments() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartments
        restApartmentMockMvc.perform(get("/api/apartments?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(apartment.getId().intValue())))
                .andExpect(jsonPath("$.[*].apartmentId").value(hasItem(DEFAULT_APARTMENT_ID.intValue())))
                .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED_STR)))
                .andExpect(jsonPath("$.[*].updated").value(hasItem(DEFAULT_UPDATED_STR)))
                .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));
    }

    @Test
    @Transactional
    public void getApartment() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get the apartment
        restApartmentMockMvc.perform(get("/api/apartments/{id}", apartment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(apartment.getId().intValue()))
            .andExpect(jsonPath("$.apartmentId").value(DEFAULT_APARTMENT_ID.intValue()))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED_STR))
            .andExpect(jsonPath("$.updated").value(DEFAULT_UPDATED_STR))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingApartment() throws Exception {
        // Get the apartment
        restApartmentMockMvc.perform(get("/api/apartments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApartment() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

		int databaseSizeBeforeUpdate = apartmentRepository.findAll().size();

        // Update the apartment
        apartment.setApartmentId(UPDATED_APARTMENT_ID);
        apartment.setCreated(UPDATED_CREATED);
        apartment.setUpdated(UPDATED_UPDATED);
        apartment.setUrl(UPDATED_URL);

        restApartmentMockMvc.perform(put("/api/apartments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(apartment)))
                .andExpect(status().isOk());

        // Validate the Apartment in the database
        List<Apartment> apartments = apartmentRepository.findAll();
        assertThat(apartments).hasSize(databaseSizeBeforeUpdate);
        Apartment testApartment = apartments.get(apartments.size() - 1);
        assertThat(testApartment.getApartmentId()).isEqualTo(UPDATED_APARTMENT_ID);
        assertThat(testApartment.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testApartment.getUpdated()).isEqualTo(UPDATED_UPDATED);
        assertThat(testApartment.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    public void deleteApartment() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

		int databaseSizeBeforeDelete = apartmentRepository.findAll().size();

        // Get the apartment
        restApartmentMockMvc.perform(delete("/api/apartments/{id}", apartment.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Apartment> apartments = apartmentRepository.findAll();
        assertThat(apartments).hasSize(databaseSizeBeforeDelete - 1);
    }
}
