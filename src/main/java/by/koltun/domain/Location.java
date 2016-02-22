package by.koltun.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Location.
 */
@Entity
@Table(name = "location")
@Document(indexName = "location")
public class Location implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @NotNull
    @Min(value = 0)
    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @NotNull
    @Min(value = 0)
    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "location", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Apartment> apartments = new HashSet<>();

    public Location() {}

    public Location(String address, Double latitude, Double longitude) {

        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location(by.koltun.domain.to.Location location) {

        this.address = location.getAddress();
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Set<Apartment> getApartments() {
        return apartments;
    }

    public void setApartments(Set<Apartment> apartments) {
        this.apartments = apartments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(address, location.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }

    @Override
    public String toString() {
        return "Location{" +
            "id=" + id +
            ", address='" + address + "'" +
            ", latitude='" + latitude + "'" +
            ", longitude='" + longitude + "'" +
            '}';
    }
}
