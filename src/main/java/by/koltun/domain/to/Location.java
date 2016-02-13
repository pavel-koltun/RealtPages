package by.koltun.domain.to;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Адрес объекта недвижимости
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Location {

    private String address;

    private Double latitude;

    private Double longitude;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Location{" +
            "address='" + address + '\'' +
            ", latitude=" + latitude +
            ", longitude=" + longitude +
            '}';
    }
}
