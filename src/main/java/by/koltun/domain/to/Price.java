package by.koltun.domain.to;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

/**
 * Цена объекта недвижимости
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Price {

    private BigDecimal usd;

    private BigDecimal byr;

    public BigDecimal getUsd() {
        return usd;
    }

    public void setUsd(BigDecimal usd) {
        this.usd = usd;
    }

    public BigDecimal getByr() {
        return byr;
    }

    public void setByr(BigDecimal byr) {
        this.byr = byr;
    }

    @Override
    public String toString() {
        return "Price{" +
            "usd=" + usd +
            ", byr=" + byr +
            '}';
    }
}
