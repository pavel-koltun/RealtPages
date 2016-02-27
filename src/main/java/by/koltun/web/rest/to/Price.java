package by.koltun.web.rest.to;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * {@link Apartment} price
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Price {

    @JsonProperty(value = "usd")
    private BigDecimal priceUsd;

    @JsonProperty(value = "byr")
    private BigDecimal priceRuble;

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

    @Override
    public String toString() {
        return "Price{" +
            "priceUsd=" + priceUsd +
            ", priceRuble=" + priceRuble +
            '}';
    }
}
