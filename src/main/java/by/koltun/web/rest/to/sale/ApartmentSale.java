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
    private Integer rooms;

    private Integer floor;

    @JsonProperty(value = "number_of_floors")
    private Integer floors;

    private Area area;

    private Seller seller;

    public Boolean getResale() {
        return isResale;
    }

    public void setResale(Boolean resale) {
        isResale = resale;
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
