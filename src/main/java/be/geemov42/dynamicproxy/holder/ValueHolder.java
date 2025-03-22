package be.geemov42.dynamicproxy.holder;

public interface ValueHolder {

    Object discriminatorKey();

    Object discriminatorValue();

    boolean isMatchingDiscriminator(final String value);
}
