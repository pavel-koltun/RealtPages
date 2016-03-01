package by.koltun.domain;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A Price.
 */
@Entity
@Table(name = "price")
@Document(indexName = "price")
public class Price implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "price_usd", nullable = false)
    private BigDecimal priceUsd;

    @NotNull
    @Column(name = "price_ruble", nullable = false)
    private BigDecimal priceRuble;

    @NotNull
    @Column(name = "updated", nullable = false)
    private ZonedDateTime updated;

    @ManyToOne
    @JoinColumn(name = "apartment_id")
    @JsonBackReference
    private Apartment apartment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPriceUsd() {
        return priceUsd;
    }

    public void setPriceUsd(BigDecimal priceUsd) {
        this.priceUsd = priceUsd;
    }

    public BigDecimal getPriceRuble() {
        return priceRuble;
    }

    public void setPriceRuble(BigDecimal priceRuble) {
        this.priceRuble = priceRuble;
    }

    public ZonedDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(ZonedDateTime updated) {
        this.updated = updated;
    }

    public Apartment getApartment() {
        return apartment;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return Objects.equals(updated, price.updated) &&
            Objects.equals(apartment, price.apartment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(updated, apartment);
    }

    @Override
    public String toString() {
        return "Price{" +
            "id=" + id +
            ", priceUsd='" + priceUsd + "'" +
            ", priceRuble='" + priceRuble + "'" +
            ", updated='" + updated + "'" +
            '}';
    }
}
