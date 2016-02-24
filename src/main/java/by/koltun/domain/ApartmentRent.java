package by.koltun.domain;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;

import by.koltun.domain.enumeration.RentType;

/**
 * A ApartmentRent.
 */
@Entity
@Table(name = "apartment_rent")
@PrimaryKeyJoinColumn(name="id")
@Document(indexName = "apartmentrent")
public class ApartmentRent extends Apartment implements Serializable {

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private RentType type;

    @NotNull
    @Column(name = "owner", nullable = false)
    private Boolean owner;

    public RentType getType() {
        return type;
    }

    public void setType(RentType type) {
        this.type = type;
    }

    public Boolean getOwner() {
        return owner;
    }

    public void setOwner(Boolean owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "ApartmentRent{" +
            "type=" + type +
            ", owner=" + owner +
            "} " + super.toString();
    }
}
