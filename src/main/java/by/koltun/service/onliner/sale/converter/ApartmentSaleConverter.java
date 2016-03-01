package by.koltun.service.onliner.sale.converter;

import by.koltun.domain.ApartmentSale;
import by.koltun.domain.enumeration.SellerType;
import by.koltun.service.onliner.converter.AbstractApartmentConverter;
import by.koltun.service.onliner.converter.LocationConverter;
import by.koltun.web.rest.to.sale.Area;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * {@link ApartmentSale} converter
 */
@Service
public class ApartmentSaleConverter extends AbstractApartmentConverter<by.koltun.web.rest.to.sale.ApartmentSale, ApartmentSale> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApartmentSaleConverter.class);

    public ApartmentSaleConverter() {

        super(ApartmentSale.class);
    }

    @Override
    public ApartmentSale convert(by.koltun.web.rest.to.sale.ApartmentSale apartment) {

        try {
            ApartmentSale result = super.convert(apartment);

            Objects.requireNonNull(result, "Converted apartment is null");

            Area area = apartment.getArea();

            Objects.requireNonNull(area, "Area is null");
            Objects.requireNonNull(area.getKitchen(), "Kitchen area is null");
            Objects.requireNonNull(area.getLiving(), "Living area is null");
            Objects.requireNonNull(area.getTotal(), "Total area is null");

            Objects.requireNonNull(apartment.getRooms(), "Rooms is null");
            Objects.requireNonNull(apartment.getFloor(), "Floor is null");
            Objects.requireNonNull(apartment.getFloors(), "Floors is null");

            Objects.requireNonNull(apartment.getResale(), "Resale is null");
            Objects.requireNonNull(apartment.getSeller(), "Seller is null");
            Objects.requireNonNull(apartment.getSeller().getType(), "Seller type is null");

            result.setAreaKitchen(area.getKitchen());
            result.setAreaLiving(area.getLiving());
            result.setAreaTotal(area.getTotal());

            result.setRooms(apartment.getRooms());
            result.setFloor(apartment.getFloor());
            result.setFloors(apartment.getFloors());

            result.setResale(apartment.getResale());


            result.setSeller(SellerType.fromCode(apartment.getSeller().getType()));

            return result;
        } catch (NullPointerException e) {

            LOGGER.error("Failed to convert apartment {}", apartment, e);
        }

        return null;
    }
}
