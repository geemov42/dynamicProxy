package be.geemov42.dynamicproxy.holder;

import be.geemov42.dynamicproxy.annotations.ColorContext;

public class ColorContextHolder implements ValueHolder {

    private static final ThreadLocal<ColorContext> context = new ThreadLocal<>();

    public static void set(ColorContext value) {
        context.set(value);
    }

    public static ColorContext get() {
        return context.get();
    }

    public static void clear() {
        context.remove();
    }

    @Override
    public Object discriminatorKey() {
        return get();
    }

    @Override
    public Object discriminatorValue() {
        return get().value();
    }

    @Override
    public boolean isMatchingDiscriminator(final String value) {
        return value.endsWith((String) discriminatorValue());
    }
}
