package by.koltun.web.rest.to.sale;

/**
 * {@link ApartmentSale} area
 */
public class Area {

    private Double total;

    private Double living;

    private Double kitchen;

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getLiving() {
        return living;
    }

    public void setLiving(Double living) {
        this.living = living;
    }

    public Double getKitchen() {
        return kitchen;
    }

    public void setKitchen(Double kitchen) {
        this.kitchen = kitchen;
    }

    @Override
    public String toString() {
        return "Area{" +
            "total=" + total +
            ", living=" + living +
            ", kitchen=" + kitchen +
            '}';
    }
}
