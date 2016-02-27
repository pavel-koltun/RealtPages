package by.koltun.web.rest.to;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * {@link ApartmentPage} info
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageInfo {

    private int limit;
    private int items;
    private int current;
    private int last;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getLast() {
        return last;
    }

    public void setLast(int last) {
        this.last = last;
    }

    @Override
    public String toString() {
        return "Page { " +
            "limit=" + limit +
            ", items=" + items +
            ", current=" + current +
            ", last=" + last +
            '}';
    }
}
