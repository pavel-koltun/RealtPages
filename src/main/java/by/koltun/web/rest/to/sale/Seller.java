package by.koltun.web.rest.to.sale;

/**
 * {@link ApartmentSale} seller
 */
public class Seller {

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Seller{" +
            "type='" + type + '\'' +
            '}';
    }
}
