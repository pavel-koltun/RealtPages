package by.koltun.web.rest.to.rent;

import by.koltun.web.rest.to.ApartmentPage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

/**
 * {@link ApartmentRent} page
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApartmentRentPage extends ApartmentPage {

    @JsonProperty(value = "apartments")
    private List<ApartmentRent> apartments;

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, defaultImpl = ApartmentRent.class)
    public List<ApartmentRent> getApartments() {
        return apartments;
    }

    public void setApartments(List<ApartmentRent> apartments) {
        this.apartments = apartments;
    }

    @Override
    public String toString() {
        return "ApartmentsRentPage { " +
            "apartments on page = " + (apartments != null ? apartments.size() : 0) + " " +
            "} " + super.toString();
    }
}
