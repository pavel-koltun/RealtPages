package by.koltun.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.ZonedDateTime;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Apartment.
 */
@Entity
@Table(name = "apartment")
@Document(indexName = "apartment")
@Inheritance(strategy = InheritanceType.JOINED)
public class Apartment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Min(value = 0)
    @Column(name = "apartment_id", nullable = false)
    private Long apartmentId;

    @NotNull
    @Column(name = "created", nullable = false)
    private ZonedDateTime created;

    @NotNull
    @Column(name = "updated", nullable = false)
    private ZonedDateTime updated;

    @Column(name = "url")
    private String url;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "apartment", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Price> prices = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(Long apartmentId) {
        this.apartmentId = apartmentId;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public ZonedDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(ZonedDateTime updated) {
        this.updated = updated;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Set<Price> getPrices() {
        return prices;
    }

    public void setPrices(Set<Price> prices) {
        this.prices = prices;
    }

    public void setPrice(Price price) {
        this.prices.add(price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Apartment apartment = (Apartment) o;
        if(apartment.apartmentId == null || apartmentId == null) {
            return false;
        }
        return Objects.equals(apartmentId, apartment.apartmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(apartmentId);
    }

    @Override
    public String toString() {
        return "Apartment{" +
            "id=" + id +
            ", apartmentId='" + apartmentId + "'" +
            ", created='" + created + "'" +
            ", updated='" + updated + "'" +
            ", url='" + url + "'" +
            '}';
    }
}
