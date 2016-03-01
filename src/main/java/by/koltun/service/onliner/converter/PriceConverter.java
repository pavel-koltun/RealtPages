package by.koltun.service.onliner.converter;

import by.koltun.domain.Price;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * {@link by.koltun.web.rest.to.Price} to {@link Price} converter
 */
@Service
public class PriceConverter implements DomainModelConverter<by.koltun.web.rest.to.Price, Price> {

    @Override
    public Price convert(by.koltun.web.rest.to.Price price) {

        Objects.requireNonNull(price, "Price is null");
        Objects.requireNonNull(price.getPriceRuble(), "Price in rubles is null");
        Objects.requireNonNull(price.getPriceUsd(), "Price in usd is null");

        Price result = new Price();
        result.setPriceUsd(price.getPriceUsd());
        result.setPriceRuble(price.getPriceRuble());

        return result;
    }
}
