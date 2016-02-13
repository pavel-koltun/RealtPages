package by.koltun.domain.to;

import by.koltun.domain.to.realt.rent.ApartmentsRentPage;
import by.koltun.domain.to.realt.sale.ApartmentsSalePage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.data.annotation.Transient;

import java.util.List;

/**
 * Created by koltun on 2/10/16.
 */
public abstract class ApartmentsPage {

    private Page page;

    private int total;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
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
            "page=" + page +
            ", total=" + total +
            '}';
    }


}
