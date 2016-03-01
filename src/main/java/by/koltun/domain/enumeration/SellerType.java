package by.koltun.domain.enumeration;

/**
 * The SellerType enumeration.
 */
public enum SellerType {
    OWNER("owner"),
    AGENT("agent"),
    BUILDER("builder"),
    OTHER("other");

    private String code;

    SellerType(String code) {

        this.code = code;
    }

    public static SellerType fromCode(String code) {

        if (code == null) {

            return SellerType.OTHER;
        }

        for (SellerType sellerType : SellerType.values()) {

            if (sellerType.code.equals(code)) {

                return sellerType;
            }
        }

        return SellerType.OTHER;
    }
}
