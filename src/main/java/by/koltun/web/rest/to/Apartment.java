package by.koltun.web.rest.to;

import by.koltun.web.rest.to.rent.ApartmentRent;
import by.koltun.web.rest.to.sale.ApartmentSale;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.*;
import static com.fasterxml.jackson.annotation.JsonSubTypes.*;

/**
 * Apartment
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "@class")
@JsonSubTypes({
    @Type(ApartmentRent.class),
    @Type(ApartmentSale.class)
})
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Apartment {

    @JsonProperty(value = "id")
    private Long apartmentId;

    @JsonProperty(value = "created_at")
    private Date created;

    @JsonProperty(value = "last_time_up")
    private Date updated;

    private String url;

    @JsonProperty
    private Price price;

    @JsonProperty
    private Location location;

    public Long getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(Long apartmentId) {
        this.apartmentId = apartmentId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {

        return "Apartment{" +
            "apartmentId=" + apartmentId +
            ", created=" + created +
            ", updated=" + updated +
            ", url='" + url + '\'' +
            ", price=" + price +
            ", location=" + location +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Apartment apartment = (Apartment) o;
        return Objects.equals(apartmentId, apartment.apartmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(apartmentId);
    }
}
