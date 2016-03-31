package by.koltun.web.rest;

import by.koltun.OnlinerRealtPagesApp;
import by.koltun.domain.ApartmentSale;
import by.koltun.repository.ApartmentSaleRepository;
import by.koltun.service.ApartmentSaleService;
import by.koltun.repository.search.ApartmentSaleSearchRepository;

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

import by.koltun.domain.enumeration.SellerType;

/**
 * Test class for the ApartmentSaleResource REST controller.
 *
 * @see ApartmentSaleResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = OnlinerRealtPagesApp.class)
@WebAppConfiguration
@IntegrationTest
public class ApartmentSaleResourceIntTest {


    private static final Boolean DEFAULT_RESALE = false;
    private static final Boolean UPDATED_RESALE = true;

    private static final Integer DEFAULT_ROOMS = 0;
    private static final Integer UPDATED_ROOMS = 1;

    private static final Integer DEFAULT_FLOOR = 0;
    private static final Integer UPDATED_FLOOR = 1;

    private static final Integer DEFAULT_FLOORS = 0;
    private static final Integer UPDATED_FLOORS = 1;

    private static final Double DEFAULT_AREA_TOTAL = 0D;
    private static final Double UPDATED_AREA_TOTAL = 1D;

    private static final Double DEFAULT_AREA_LIVING = 0D;
    private static final Double UPDATED_AREA_LIVING = 1D;

    private static final Double DEFAULT_AREA_KITCHEN = 0D;
    private static final Double UPDATED_AREA_KITCHEN = 1D;

    private static final SellerType DEFAULT_SELLER = SellerType.OWNER;
    private static final SellerType UPDATED_SELLER = SellerType.AGENT;

    @Inject
    private ApartmentSaleRepository apartmentSaleRepository;

    @Inject
    private ApartmentSaleService apartmentSaleService;

    @Inject
    private ApartmentSaleSearchRepository apartmentSaleSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restApartmentSaleMockMvc;

    private ApartmentSale apartmentSale;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ApartmentSaleResource apartmentSaleResource = new ApartmentSaleResource();
        ReflectionTestUtils.setField(apartmentSaleResource, "apartmentSaleService", apartmentSaleService);
        this.restApartmentSaleMockMvc = MockMvcBuilders.standaloneSetup(apartmentSaleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        apartmentSaleSearchRepository.deleteAll();
        apartmentSale = new ApartmentSale();
        apartmentSale.setResale(DEFAULT_RESALE);
        apartmentSale.setRooms(DEFAULT_ROOMS);
        apartmentSale.setFloor(DEFAULT_FLOOR);
        apartmentSale.setFloors(DEFAULT_FLOORS);
        apartmentSale.setAreaTotal(DEFAULT_AREA_TOTAL);
        apartmentSale.setAreaLiving(DEFAULT_AREA_LIVING);
        apartmentSale.setAreaKitchen(DEFAULT_AREA_KITCHEN);
        apartmentSale.setSeller(DEFAULT_SELLER);
    }

    @Test
    @Transactional
    public void createApartmentSale() throws Exception {
        int databaseSizeBeforeCreate = apartmentSaleRepository.findAll().size();

        // Create the ApartmentSale

        restApartmentSaleMockMvc.perform(post("/api/apartment-sales")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(apartmentSale)))
                .andExpect(status().isCreated());

        // Validate the ApartmentSale in the database
        List<ApartmentSale> apartmentSales = apartmentSaleRepository.findAll();
        assertThat(apartmentSales).hasSize(databaseSizeBeforeCreate + 1);
        ApartmentSale testApartmentSale = apartmentSales.get(apartmentSales.size() - 1);
        assertThat(testApartmentSale.getResale()).isEqualTo(DEFAULT_RESALE);
        assertThat(testApartmentSale.getRooms()).isEqualTo(DEFAULT_ROOMS);
        assertThat(testApartmentSale.getFloor()).isEqualTo(DEFAULT_FLOOR);
        assertThat(testApartmentSale.getFloors()).isEqualTo(DEFAULT_FLOORS);
        assertThat(testApartmentSale.getAreaTotal()).isEqualTo(DEFAULT_AREA_TOTAL);
        assertThat(testApartmentSale.getAreaLiving()).isEqualTo(DEFAULT_AREA_LIVING);
        assertThat(testApartmentSale.getAreaKitchen()).isEqualTo(DEFAULT_AREA_KITCHEN);
        assertThat(testApartmentSale.getSeller()).isEqualTo(DEFAULT_SELLER);

        // Validate the ApartmentSale in ElasticSearch
        ApartmentSale apartmentSaleEs = apartmentSaleSearchRepository.findOne(testApartmentSale.getId());
        assertThat(apartmentSaleEs).isEqualToComparingFieldByField(testApartmentSale);
    }

    @Test
    @Transactional
    public void checkResaleIsRequired() throws Exception {
        int databaseSizeBeforeTest = apartmentSaleRepository.findAll().size();
        // set the field null
        apartmentSale.setResale(null);

        // Create the ApartmentSale, which fails.

        restApartmentSaleMockMvc.perform(post("/api/apartment-sales")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(apartmentSale)))
                .andExpect(status().isBadRequest());

        List<ApartmentSale> apartmentSales = apartmentSaleRepository.findAll();
        assertThat(apartmentSales).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRoomsIsRequired() throws Exception {
        int databaseSizeBeforeTest = apartmentSaleRepository.findAll().size();
        // set the field null
        apartmentSale.setRooms(null);

        // Create the ApartmentSale, which fails.

        restApartmentSaleMockMvc.perform(post("/api/apartment-sales")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(apartmentSale)))
                .andExpect(status().isBadRequest());

        List<ApartmentSale> apartmentSales = apartmentSaleRepository.findAll();
        assertThat(apartmentSales).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFloorIsRequired() throws Exception {
        int databaseSizeBeforeTest = apartmentSaleRepository.findAll().size();
        // set the field null
        apartmentSale.setFloor(null);

        // Create the ApartmentSale, which fails.

        restApartmentSaleMockMvc.perform(post("/api/apartment-sales")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(apartmentSale)))
                .andExpect(status().isBadRequest());

        List<ApartmentSale> apartmentSales = apartmentSaleRepository.findAll();
        assertThat(apartmentSales).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFloorsIsRequired() throws Exception {
        int databaseSizeBeforeTest = apartmentSaleRepository.findAll().size();
        // set the field null
        apartmentSale.setFloors(null);

        // Create the ApartmentSale, which fails.

        restApartmentSaleMockMvc.perform(post("/api/apartment-sales")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(apartmentSale)))
                .andExpect(status().isBadRequest());

        List<ApartmentSale> apartmentSales = apartmentSaleRepository.findAll();
        assertThat(apartmentSales).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAreaTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = apartmentSaleRepository.findAll().size();
        // set the field null
        apartmentSale.setAreaTotal(null);

        // Create the ApartmentSale, which fails.

        restApartmentSaleMockMvc.perform(post("/api/apartment-sales")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(apartmentSale)))
                .andExpect(status().isBadRequest());

        List<ApartmentSale> apartmentSales = apartmentSaleRepository.findAll();
        assertThat(apartmentSales).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAreaLivingIsRequired() throws Exception {
        int databaseSizeBeforeTest = apartmentSaleRepository.findAll().size();
        // set the field null
        apartmentSale.setAreaLiving(null);

        // Create the ApartmentSale, which fails.

        restApartmentSaleMockMvc.perform(post("/api/apartment-sales")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(apartmentSale)))
                .andExpect(status().isBadRequest());

        List<ApartmentSale> apartmentSales = apartmentSaleRepository.findAll();
        assertThat(apartmentSales).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAreaKitchenIsRequired() throws Exception {
        int databaseSizeBeforeTest = apartmentSaleRepository.findAll().size();
        // set the field null
        apartmentSale.setAreaKitchen(null);

        // Create the ApartmentSale, which fails.

        restApartmentSaleMockMvc.perform(post("/api/apartment-sales")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(apartmentSale)))
                .andExpect(status().isBadRequest());

        List<ApartmentSale> apartmentSales = apartmentSaleRepository.findAll();
        assertThat(apartmentSales).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSellerIsRequired() throws Exception {
        int databaseSizeBeforeTest = apartmentSaleRepository.findAll().size();
        // set the field null
        apartmentSale.setSeller(null);

        // Create the ApartmentSale, which fails.

        restApartmentSaleMockMvc.perform(post("/api/apartment-sales")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(apartmentSale)))
                .andExpect(status().isBadRequest());

        List<ApartmentSale> apartmentSales = apartmentSaleRepository.findAll();
        assertThat(apartmentSales).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllApartmentSales() throws Exception {
        // Initialize the database
        apartmentSaleRepository.saveAndFlush(apartmentSale);

        // Get all the apartmentSales
        restApartmentSaleMockMvc.perform(get("/api/apartment-sales?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(apartmentSale.getId().intValue())))
                .andExpect(jsonPath("$.[*].resale").value(hasItem(DEFAULT_RESALE.booleanValue())))
                .andExpect(jsonPath("$.[*].rooms").value(hasItem(DEFAULT_ROOMS)))
                .andExpect(jsonPath("$.[*].floor").value(hasItem(DEFAULT_FLOOR)))
                .andExpect(jsonPath("$.[*].floors").value(hasItem(DEFAULT_FLOORS)))
                .andExpect(jsonPath("$.[*].areaTotal").value(hasItem(DEFAULT_AREA_TOTAL.doubleValue())))
                .andExpect(jsonPath("$.[*].areaLiving").value(hasItem(DEFAULT_AREA_LIVING.doubleValue())))
                .andExpect(jsonPath("$.[*].areaKitchen").value(hasItem(DEFAULT_AREA_KITCHEN.doubleValue())))
                .andExpect(jsonPath("$.[*].seller").value(hasItem(DEFAULT_SELLER.toString())));
    }

    @Test
    @Transactional
    public void getApartmentSale() throws Exception {
        // Initialize the database
        apartmentSaleRepository.saveAndFlush(apartmentSale);

        // Get the apartmentSale
        restApartmentSaleMockMvc.perform(get("/api/apartment-sales/{id}", apartmentSale.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(apartmentSale.getId().intValue()))
            .andExpect(jsonPath("$.resale").value(DEFAULT_RESALE.booleanValue()))
            .andExpect(jsonPath("$.rooms").value(DEFAULT_ROOMS))
            .andExpect(jsonPath("$.floor").value(DEFAULT_FLOOR))
            .andExpect(jsonPath("$.floors").value(DEFAULT_FLOORS))
            .andExpect(jsonPath("$.areaTotal").value(DEFAULT_AREA_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.areaLiving").value(DEFAULT_AREA_LIVING.doubleValue()))
            .andExpect(jsonPath("$.areaKitchen").value(DEFAULT_AREA_KITCHEN.doubleValue()))
            .andExpect(jsonPath("$.seller").value(DEFAULT_SELLER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingApartmentSale() throws Exception {
        // Get the apartmentSale
        restApartmentSaleMockMvc.perform(get("/api/apartment-sales/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApartmentSale() throws Exception {
        // Initialize the database
        apartmentSaleService.save(apartmentSale);

        int databaseSizeBeforeUpdate = apartmentSaleRepository.findAll().size();

        // Update the apartmentSale
        ApartmentSale updatedApartmentSale = new ApartmentSale();
        updatedApartmentSale.setId(apartmentSale.getId());
        updatedApartmentSale.setResale(UPDATED_RESALE);
        updatedApartmentSale.setRooms(UPDATED_ROOMS);
        updatedApartmentSale.setFloor(UPDATED_FLOOR);
        updatedApartmentSale.setFloors(UPDATED_FLOORS);
        updatedApartmentSale.setAreaTotal(UPDATED_AREA_TOTAL);
        updatedApartmentSale.setAreaLiving(UPDATED_AREA_LIVING);
        updatedApartmentSale.setAreaKitchen(UPDATED_AREA_KITCHEN);
        updatedApartmentSale.setSeller(UPDATED_SELLER);

        restApartmentSaleMockMvc.perform(put("/api/apartment-sales")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedApartmentSale)))
                .andExpect(status().isOk());

        // Validate the ApartmentSale in the database
        List<ApartmentSale> apartmentSales = apartmentSaleRepository.findAll();
        assertThat(apartmentSales).hasSize(databaseSizeBeforeUpdate);
        ApartmentSale testApartmentSale = apartmentSales.get(apartmentSales.size() - 1);
        assertThat(testApartmentSale.getResale()).isEqualTo(UPDATED_RESALE);
        assertThat(testApartmentSale.getRooms()).isEqualTo(UPDATED_ROOMS);
        assertThat(testApartmentSale.getFloor()).isEqualTo(UPDATED_FLOOR);
        assertThat(testApartmentSale.getFloors()).isEqualTo(UPDATED_FLOORS);
        assertThat(testApartmentSale.getAreaTotal()).isEqualTo(UPDATED_AREA_TOTAL);
        assertThat(testApartmentSale.getAreaLiving()).isEqualTo(UPDATED_AREA_LIVING);
        assertThat(testApartmentSale.getAreaKitchen()).isEqualTo(UPDATED_AREA_KITCHEN);
        assertThat(testApartmentSale.getSeller()).isEqualTo(UPDATED_SELLER);

        // Validate the ApartmentSale in ElasticSearch
        ApartmentSale apartmentSaleEs = apartmentSaleSearchRepository.findOne(testApartmentSale.getId());
        assertThat(apartmentSaleEs).isEqualToComparingFieldByField(testApartmentSale);
    }

    @Test
    @Transactional
    public void deleteApartmentSale() throws Exception {
        // Initialize the database
        apartmentSaleService.save(apartmentSale);

        int databaseSizeBeforeDelete = apartmentSaleRepository.findAll().size();

        // Get the apartmentSale
        restApartmentSaleMockMvc.perform(delete("/api/apartment-sales/{id}", apartmentSale.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean apartmentSaleExistsInEs = apartmentSaleSearchRepository.exists(apartmentSale.getId());
        assertThat(apartmentSaleExistsInEs).isFalse();

        // Validate the database is empty
        List<ApartmentSale> apartmentSales = apartmentSaleRepository.findAll();
        assertThat(apartmentSales).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchApartmentSale() throws Exception {
        // Initialize the database
        apartmentSaleService.save(apartmentSale);

        // Search the apartmentSale
        restApartmentSaleMockMvc.perform(get("/api/_search/apartment-sales?query=id:" + apartmentSale.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(apartmentSale.getId().intValue())))
            .andExpect(jsonPath("$.[*].resale").value(hasItem(DEFAULT_RESALE.booleanValue())))
            .andExpect(jsonPath("$.[*].rooms").value(hasItem(DEFAULT_ROOMS)))
            .andExpect(jsonPath("$.[*].floor").value(hasItem(DEFAULT_FLOOR)))
            .andExpect(jsonPath("$.[*].floors").value(hasItem(DEFAULT_FLOORS)))
            .andExpect(jsonPath("$.[*].areaTotal").value(hasItem(DEFAULT_AREA_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].areaLiving").value(hasItem(DEFAULT_AREA_LIVING.doubleValue())))
            .andExpect(jsonPath("$.[*].areaKitchen").value(hasItem(DEFAULT_AREA_KITCHEN.doubleValue())))
            .andExpect(jsonPath("$.[*].seller").value(hasItem(DEFAULT_SELLER.toString())));
    }
}
