package by.koltun.domain.to.realt.rent;

import by.koltun.domain.to.Apartment;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Арендуемое помещение
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
}
