package by.koltun.service;

import by.koltun.domain.to.Location;
import by.koltun.domain.to.Page;
import by.koltun.domain.to.Price;
import by.koltun.domain.to.realt.rent.ApartmentRent;
import by.koltun.domain.to.realt.rent.ApartmentsRentPage;
import by.koltun.domain.to.realt.rent.Contact;
import by.koltun.domain.to.realt.sale.ApartmentSale;
import by.koltun.domain.to.realt.sale.ApartmentsSalePage;
import by.koltun.domain.to.realt.sale.Area;
import by.koltun.domain.to.realt.sale.Seller;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Тест проверки сериализуемости данных {@link ApartmentRent}, {@link ApartmentSale}
 */
public class JsonSerializationTest {

    @Test
    public void testCanSerializeApartmentSale() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY);

        ApartmentSale apartmentSale = new ApartmentSale();

        apartmentSale.setResale(Boolean.FALSE);

        apartmentSale.setFloor(1L);
        apartmentSale.setFloors(19L);
        apartmentSale.setRooms(2L);

        Area area = new Area();
        area.setTotal(55D);
        area.setKitchen(10D);
        area.setLiving(45D);

        apartmentSale.setArea(area);

        Seller seller = new Seller();
        seller.setType("testSellerType");

        apartmentSale.setSeller(seller);

        Location location = new Location();
        location.setAddress("testRentAddress");
        location.setLatitude(12345D);
        location.setLongitude(12345D);

        apartmentSale.setLocation(location);

        Price price = new Price();
        price.setByr(BigDecimal.ONE);
        price.setUsd(BigDecimal.TEN);

        apartmentSale.setPrice(price);

        apartmentSale.setApartmentId(1L);
        apartmentSale.setCreated(new Date());
        apartmentSale.setUpdated(new Date());
        apartmentSale.setUrl("testSaleUrl");

        boolean can = mapper.canSerialize(ApartmentSale.class);
        Assert.assertTrue(can);

        String json = mapper.writeValueAsString(apartmentSale);

        ApartmentSale result = mapper.readValue(json, ApartmentSale.class);
        Assert.assertNotNull(result);
    }

    @Test
    public void testCanSerializeApartmentRent() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY);

        ApartmentRent apartmentRent = new ApartmentRent();

        apartmentRent.setRentType("testRentType");

        Contact contact = new Contact();
        contact.setOwner(Boolean.FALSE);

        apartmentRent.setContact(contact);

        Location location = new Location();
        location.setAddress("testRentAddress");
        location.setLatitude(12345D);
        location.setLongitude(12345D);

        apartmentRent.setLocation(location);

        Price price = new Price();
        price.setByr(BigDecimal.ONE);
        price.setUsd(BigDecimal.TEN);

        apartmentRent.setPrice(price);

        apartmentRent.setApartmentId(1L);
        apartmentRent.setCreated(new Date());
        apartmentRent.setUpdated(new Date());
        apartmentRent.setUrl("testRentUrl");

        boolean can = mapper.canSerialize(ApartmentRent.class);
        Assert.assertTrue(can);

        String json = mapper.writeValueAsString(apartmentRent);

        ApartmentRent result = mapper.readValue(json, ApartmentRent.class);
        Assert.assertNotNull(result);
    }

    @Test
    public void testCanSerializeApartmentsSalePage() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY);

        ApartmentsSalePage apartmentsSalePage = new ApartmentsSalePage();

        Page page = new Page();
        page.setCurrent(1);
        page.setItems(30);
        page.setLimit(30);
        page.setLast(295);

        apartmentsSalePage.setPage(page);

        apartmentsSalePage.setTotal(7000);

        List<ApartmentSale> apartments = new ArrayList<>();

        for (int i = 0; i < 5; i++) {

            ApartmentSale apartmentSale = new ApartmentSale();

            apartments.add(apartmentSale);
        }

        apartmentsSalePage.setApartments(apartments);

        boolean can = mapper.canSerialize(ApartmentsSalePage.class);
        Assert.assertTrue(can);

        String json = mapper.writeValueAsString(apartmentsSalePage);

        ApartmentsSalePage result = mapper.readValue(json, ApartmentsSalePage.class);
        Assert.assertNotNull(result);
    }

    @Test
    public void testCanSerializeApartmentsRentPage() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY);

        ApartmentsRentPage apartmentsRentPage = new ApartmentsRentPage();

        Page page = new Page();
        page.setCurrent(1);
        page.setItems(30);
        page.setLimit(30);
        page.setLast(295);

        apartmentsRentPage.setPage(page);

        apartmentsRentPage.setTotal(7000);

        List<ApartmentRent> apartments = new ArrayList<>();

        for (int i = 0; i < 5; i++) {

            ApartmentRent apartmentRent = new ApartmentRent();

            apartments.add(apartmentRent);
        }

        apartmentsRentPage.setApartments(apartments);

        boolean can = mapper.canSerialize(ApartmentsRentPage.class);
        Assert.assertTrue(can);

        String json = mapper.writeValueAsString(apartmentsRentPage);

        ApartmentsRentPage result = mapper.readValue(json, ApartmentsRentPage.class);
        Assert.assertNotNull(result);
    }
}
