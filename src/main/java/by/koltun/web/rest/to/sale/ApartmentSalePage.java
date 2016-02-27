package by.koltun.web.rest.to.sale;

import by.koltun.web.rest.to.ApartmentPage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

/**
 * {@link ApartmentSale} page
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApartmentSalePage extends ApartmentPage {

    @JsonProperty(value = "apartments")
    private List<ApartmentSale> apartments;

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, defaultImpl = ApartmentSale.class)
    public List<ApartmentSale> getApartments() {
        return apartments;
    }

    public void setApartments(List<ApartmentSale> apartments) {
        this.apartments = apartments;
    }

    @Override
    public String toString() {
        return "ApartmentsSalePage { " +
            "apartments on page = " + (apartments != null ? apartments.size() : 0) + " " +
            "} " + super.toString();
    }
}
