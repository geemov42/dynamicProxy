package be.geemov42.dynamicproxy.annotations;

import static be.geemov42.dynamicproxy.annotations.Constants.BLUE;
import static be.geemov42.dynamicproxy.annotations.Constants.GREEN;

public enum ColorContext {


    CTX_BLUE(BLUE), CTX_GREEN(GREEN);

    private final String value;

    ColorContext(final String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static ColorContext fromValue(final String value) {
        for (ColorContext colorContext : ColorContext.values()) {
            if (colorContext.value().equals(value)) {
                return colorContext;
            }
        }

        throw new IllegalArgumentException("The value doesn't match any enum value");
    }
}
