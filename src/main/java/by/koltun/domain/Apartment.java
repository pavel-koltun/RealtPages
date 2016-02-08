package by.koltun.domain;

import java.time.ZonedDateTime;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Apartment.
 */
@Entity
@Table(name = "apartment")
@Document(indexName = "apartment")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Apartment apartment = (Apartment) o;
        if(apartment.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, apartment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
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
