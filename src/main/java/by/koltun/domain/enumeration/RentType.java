package by.koltun.domain.enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The RentType enumeration.
 */
public enum RentType {

    ROOM("room"),
    ONE_ROOM("1_room"),
    TWO_ROOMS("2_rooms"),
    THREE_ROOMS("3_rooms"),
    FOUR_ROOMS("4_rooms"),
    FIVE_ROOMS("5_rooms"),
    SIX_ROOMS("6_rooms"),
    OTHER("other");

    private static final Logger LOGGER = LoggerFactory.getLogger(RentType.class);

    private String code;

    RentType(String code) {
        this.code = code;
    }

    public static RentType fromCode(String code) {

        RentType result = RentType.OTHER;

        if (code == null) {

            return result;
        }

        for (RentType rentType : RentType.values()) {

            if (rentType.code.equals(code)) {

                return rentType;
            }
        }

        return result;
    }
}
