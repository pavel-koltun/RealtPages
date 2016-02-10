package by.koltun.domain;

import java.time.ZonedDateTime;
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
    @Min(value = 0)
    @Column(name = "price_usd", precision=10, scale=2, nullable = false)
    private BigDecimal priceUsd;

    @NotNull
    @Min(value = 0)
    @Column(name = "price_ruble", precision=10, scale=2, nullable = false)
    private BigDecimal priceRuble;

    @NotNull
    @Column(name = "created", nullable = false)
    private ZonedDateTime created;

    @ManyToOne
    @JoinColumn(name = "apartment_id")
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

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public Apartment getApartment() {
        return apartment;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Price price = (Price) o;
        if(price.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, price.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Price{" +
            "id=" + id +
            ", priceUsd='" + priceUsd + "'" +
            ", priceRuble='" + priceRuble + "'" +
            ", created='" + created + "'" +
            '}';
    }
}
