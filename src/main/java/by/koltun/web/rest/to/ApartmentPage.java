package by.koltun.web.rest.to;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * {@link Apartment} page
 */
public abstract class ApartmentPage {

    @JsonProperty(value = "page")
    private PageInfo pageInfo;

    private int total;

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "ApartmentsPage{" +
            "page=" + pageInfo +
            ", total=" + total +
            '}';
    }


}
