package by.koltun.domain.to.realt.rent;

import by.koltun.domain.to.ApartmentsPage;
import by.koltun.domain.to.realt.sale.ApartmentSale;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

/**
 * Страница с {@link ApartmentRent}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApartmentsRentPage extends ApartmentsPage {

    @JsonProperty(value = "apartments")
    private List<ApartmentRent> apartments;

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, defaultImpl = ApartmentRent.class)
    public List<ApartmentRent> getApartments() {
        return apartments;
    }

    public void setApartments(List<ApartmentRent> apartments) {
        this.apartments = apartments;
    }
}
