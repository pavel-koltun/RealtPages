package by.koltun.domain;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import by.koltun.domain.enumeration.RentType;

/**
 * A ApartmentRent.
 */
@Entity
@Table(name = "apartment_rent")
@Document(indexName = "apartmentrent")
public class ApartmentRent extends Apartment implements Serializable {

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private RentType type;

    @NotNull
    @Column(name = "is_owner", nullable = false)
    private Boolean isOwner;

    public RentType getType() {
        return type;
    }

    public void setType(RentType type) {
        this.type = type;
    }

    public Boolean getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(Boolean isOwner) {
        this.isOwner = isOwner;
    }

    @Override
    public String toString() {
        return "ApartmentRent{" +
            "type=" + type +
            ", isOwner=" + isOwner +
            "} " + super.toString();
    }
}
