package by.koltun.web.rest.to.rent;


import by.koltun.web.rest.to.Apartment;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Apartment for rent
 */
public class ApartmentRent extends Apartment {

    @JsonProperty(value = "rent_type")
    private String rentType;

    @JsonProperty(value = "contact")
    private Contact contact;

    public String getRentType() {
        return rentType;
    }

    public void setRentType(String rentType) {
        this.rentType = rentType;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {

        return "ApartmentRent{" +
            "rentType='" + rentType + '\'' +
            ", contact=" + contact +
            "} " + super.toString();
    }
}
