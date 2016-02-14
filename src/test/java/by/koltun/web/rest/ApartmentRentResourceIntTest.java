package by.koltun.web.rest;

import by.koltun.Application;
import by.koltun.domain.ApartmentRent;
import by.koltun.repository.ApartmentRentRepository;
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
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ApartmentRentResourceIntTest {

    
    private static final RentType DEFAULT_TYPE = RentType.ROOM;
    private static final RentType UPDATED_TYPE = RentType.ONE_ROOM;

    private static final Boolean DEFAULT_IS_OWNER = false;
    private static final Boolean UPDATED_IS_OWNER = true;

    @Inject
    private ApartmentRentRepository apartmentRentRepository;

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
        ReflectionTestUtils.setField(apartmentRentResource, "apartmentRentSearchRepository", apartmentRentSearchRepository);
        ReflectionTestUtils.setField(apartmentRentResource, "apartmentRentRepository", apartmentRentRepository);
        this.restApartmentRentMockMvc = MockMvcBuilders.standaloneSetup(apartmentRentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        apartmentRent = new ApartmentRent();
        apartmentRent.setType(DEFAULT_TYPE);
        apartmentRent.setIsOwner(DEFAULT_IS_OWNER);
    }

    @Test
    @Transactional
    public void createApartmentRent() throws Exception {
        int databaseSizeBeforeCreate = apartmentRentRepository.findAll().size();

        // Create the ApartmentRent

        restApartmentRentMockMvc.perform(post("/api/apartmentRents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(apartmentRent)))
                .andExpect(status().isCreated());

        // Validate the ApartmentRent in the database
        List<ApartmentRent> apartmentRents = apartmentRentRepository.findAll();
        assertThat(apartmentRents).hasSize(databaseSizeBeforeCreate + 1);
        ApartmentRent testApartmentRent = apartmentRents.get(apartmentRents.size() - 1);
        assertThat(testApartmentRent.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testApartmentRent.getIsOwner()).isEqualTo(DEFAULT_IS_OWNER);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = apartmentRentRepository.findAll().size();
        // set the field null
        apartmentRent.setType(null);

        // Create the ApartmentRent, which fails.

        restApartmentRentMockMvc.perform(post("/api/apartmentRents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(apartmentRent)))
                .andExpect(status().isBadRequest());

        List<ApartmentRent> apartmentRents = apartmentRentRepository.findAll();
        assertThat(apartmentRents).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsOwnerIsRequired() throws Exception {
        int databaseSizeBeforeTest = apartmentRentRepository.findAll().size();
        // set the field null
        apartmentRent.setIsOwner(null);

        // Create the ApartmentRent, which fails.

        restApartmentRentMockMvc.perform(post("/api/apartmentRents")
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
        restApartmentRentMockMvc.perform(get("/api/apartmentRents?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(apartmentRent.getId().intValue())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].isOwner").value(hasItem(DEFAULT_IS_OWNER.booleanValue())));
    }

    @Test
    @Transactional
    public void getApartmentRent() throws Exception {
        // Initialize the database
        apartmentRentRepository.saveAndFlush(apartmentRent);

        // Get the apartmentRent
        restApartmentRentMockMvc.perform(get("/api/apartmentRents/{id}", apartmentRent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(apartmentRent.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.isOwner").value(DEFAULT_IS_OWNER.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingApartmentRent() throws Exception {
        // Get the apartmentRent
        restApartmentRentMockMvc.perform(get("/api/apartmentRents/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApartmentRent() throws Exception {
        // Initialize the database
        apartmentRentRepository.saveAndFlush(apartmentRent);

		int databaseSizeBeforeUpdate = apartmentRentRepository.findAll().size();

        // Update the apartmentRent
        apartmentRent.setType(UPDATED_TYPE);
        apartmentRent.setIsOwner(UPDATED_IS_OWNER);

        restApartmentRentMockMvc.perform(put("/api/apartmentRents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(apartmentRent)))
                .andExpect(status().isOk());

        // Validate the ApartmentRent in the database
        List<ApartmentRent> apartmentRents = apartmentRentRepository.findAll();
        assertThat(apartmentRents).hasSize(databaseSizeBeforeUpdate);
        ApartmentRent testApartmentRent = apartmentRents.get(apartmentRents.size() - 1);
        assertThat(testApartmentRent.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testApartmentRent.getIsOwner()).isEqualTo(UPDATED_IS_OWNER);
    }

    @Test
    @Transactional
    public void deleteApartmentRent() throws Exception {
        // Initialize the database
        apartmentRentRepository.saveAndFlush(apartmentRent);

		int databaseSizeBeforeDelete = apartmentRentRepository.findAll().size();

        // Get the apartmentRent
        restApartmentRentMockMvc.perform(delete("/api/apartmentRents/{id}", apartmentRent.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ApartmentRent> apartmentRents = apartmentRentRepository.findAll();
        assertThat(apartmentRents).hasSize(databaseSizeBeforeDelete - 1);
    }
}
