package by.koltun.web.rest.to.sale;

import by.koltun.web.rest.to.Apartment;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Apartment for sale
 */
public class ApartmentSale extends Apartment {

    @JsonProperty(value = "resale")
    private Boolean isResale;

    @JsonProperty(value = "number_of_rooms")
    private Long rooms;

    private Long floor;

    @JsonProperty(value = "number_of_floors")
    private Long floors;

    private Area area;

    private Seller seller;

    public Boolean getResale() {
        return isResale;
    }

    public void setResale(Boolean resale) {
        isResale = resale;
    }

    public Long getRooms() {
        return rooms;
    }

    public void setRooms(Long rooms) {
        this.rooms = rooms;
    }

    public Long getFloor() {
        return floor;
    }

    public void setFloor(Long floor) {
        this.floor = floor;
    }

    public Long getFloors() {
        return floors;
    }

    public void setFloors(Long floors) {
        this.floors = floors;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    @Override
    public String toString() {
        return "ApartmentSale{" +
            "isResale=" + isResale +
            ", rooms=" + rooms +
            ", floor=" + floor +
            ", floors=" + floors +
            ", area=" + area +
            ", seller=" + seller +
            "} " + super.toString();
    }
}
