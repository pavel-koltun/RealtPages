package by.koltun.domain;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;

import by.koltun.domain.enumeration.SellerType;

/**
 * A ApartmentSale.
 */
@Entity
@Table(name = "apartment_sale")
@PrimaryKeyJoinColumn(name="id")
@Document(indexName = "apartmentsale")
public class ApartmentSale extends Apartment implements Serializable {

    @NotNull
    @Column(name = "resale", nullable = false)
    private Boolean resale;

    @NotNull
    @Min(value = 0)
    @Column(name = "rooms", nullable = false)
    private Integer rooms;

    @NotNull
    @Min(value = 0)
    @Column(name = "floor", nullable = false)
    private Integer floor;

    @NotNull
    @Min(value = 0)
    @Column(name = "floors", nullable = false)
    private Integer floors;

    @NotNull
    @Min(value = 0)
    @Column(name = "area_total", nullable = false)
    private Double areaTotal;

    @NotNull
    @Min(value = 0)
    @Column(name = "area_living", nullable = false)
    private Double areaLiving;

    @NotNull
    @Min(value = 0)
    @Column(name = "area_kitchen", nullable = false)
    private Double areaKitchen;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "seller", nullable = false)
    private SellerType seller;

    public Boolean getResale() {
        return resale;
    }

    public void setResale(Boolean resale) {
        this.resale = resale;
    }

    public Integer getRooms() {
        return rooms;
    }

    public void setRooms(Integer rooms) {
        this.rooms = rooms;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Integer getFloors() {
        return floors;
    }

    public void setFloors(Integer floors) {
        this.floors = floors;
    }

    public Double getAreaTotal() {
        return areaTotal;
    }

    public void setAreaTotal(Double areaTotal) {
        this.areaTotal = areaTotal;
    }

    public Double getAreaLiving() {
        return areaLiving;
    }

    public void setAreaLiving(Double areaLiving) {
        this.areaLiving = areaLiving;
    }

    public Double getAreaKitchen() {
        return areaKitchen;
    }

    public void setAreaKitchen(Double areaKitchen) {
        this.areaKitchen = areaKitchen;
    }

    public SellerType getSeller() {
        return seller;
    }

    public void setSeller(SellerType seller) {
        this.seller = seller;
    }

    @Override
    public String toString() {
        return "ApartmentSale{" +
            "resale=" + resale +
            ", rooms=" + rooms +
            ", floor=" + floor +
            ", floors=" + floors +
            ", areaTotal=" + areaTotal +
            ", areaLiving=" + areaLiving +
            ", areaKitchen=" + areaKitchen +
            ", seller=" + seller +
            "} " + super.toString();
    }
}
