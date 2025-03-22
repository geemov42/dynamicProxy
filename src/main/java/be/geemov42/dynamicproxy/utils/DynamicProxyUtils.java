package be.geemov42.dynamicproxy.utils;

import be.geemov42.dynamicproxy.holder.ValueHolder;
import lombok.NonNull;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

public class DynamicProxyUtils {

    private static final ClassLoader PROXY_CLASS_LOADER = DynamicProxyUtils.class.getClassLoader();
    private static final String DISCRIMINATOR_METHOD_KEY = "discriminatorKey";
    private static final String IS_MATCHING_DISCRIMINATOR_METHOD = "isMatchingDiscriminator";

    private DynamicProxyUtils() {
    }

    /**
     * Create a Spring proxy to allow contextualized bean resolution.
     */
    public static <T, VH extends ValueHolder> T createSpringProxy(
            @NonNull final Class<T> targetClass,
            @NonNull final ApplicationContext appContext,
            @NonNull final String beanBaseName,
            @NonNull final Class<VH> valueHolderClass
    ) {
        return targetClass.cast(
                Proxy.newProxyInstance(
                        PROXY_CLASS_LOADER,
                        new Class<?>[]{targetClass},
                        new SpringInvocationHandler<>(targetClass, valueHolderClass, appContext, beanBaseName)
                )
        );
    }

    /**
     * Create a proxy with a Map as the backing instance provider.
     */
    public static <T, VH extends ValueHolder> T createMapProxy(
            @NonNull final Class<T> targetClass,
            @NonNull final Class<VH> valueHolderClass,
            @NonNull final Map<Object, Object> instances
    ) {
        return targetClass.cast(
                Proxy.newProxyInstance(
                        PROXY_CLASS_LOADER,
                        new Class<?>[]{targetClass},
                        new MapInvocationHandler<>(targetClass, valueHolderClass, instances)
                )
        );
    }

    /**
     * AbstractInvocationHandler provides base logic for resolving instances dynamically.
     */
    private abstract static class AbstractInvocationHandler<T, VH extends ValueHolder> implements InvocationHandler {

        protected final Class<T> targetClass;
        protected final Class<VH> valueHolderClass;

        protected AbstractInvocationHandler(
                @NonNull final Class<T> targetClass,
                @NonNull final Class<VH> valueHolderClass
        ) {
            this.targetClass = targetClass;
            this.valueHolderClass = valueHolderClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            final Object discriminatorKey = invokeStaticMethod(getStaticMethod(DISCRIMINATOR_METHOD_KEY));
            final Object realInstance = getInstance(discriminatorKey);
            return method.invoke(realInstance, args);
        }

        protected Method getStaticMethod(final String methodName, Class<?>... parameterTypes) {
            try {
                return valueHolderClass.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) {
                throw new NoSuchMethodError("Method '" + methodName + "' not found in " + valueHolderClass.getName());
            }
        }

        protected Object invokeStaticMethod(final Method method, Object... args) {
            try {
                return method.invoke(valueHolderClass.getDeclaredConstructor().newInstance(), args);
            } catch (ReflectiveOperationException e) {
                throw new ProxyInstanceNotFoundException("Error invoking ValueHolder static method", e);
            }
        }

        protected abstract Object getInstance(Object discriminatorKey);
    }

    /**
     * Map-based invocation handler for proxy instances.
     */
    private static class MapInvocationHandler<T, VH extends ValueHolder> extends AbstractInvocationHandler<T, VH> {

        private final Map<Object, Object> instanceByKeyMap;

        public MapInvocationHandler(
                @NonNull final Class<T> targetClass,
                @NonNull final Class<VH> valueHolderClass,
                @NonNull final Map<Object, Object> instanceByKeyMap
        ) {
            super(targetClass, valueHolderClass);
            this.instanceByKeyMap = instanceByKeyMap;
        }

        @Override
        protected Object getInstance(@NonNull final Object discriminatorKey) {
            return targetClass.cast(instanceByKeyMap.get(discriminatorKey));
        }
    }

    /**
     * Spring-specific invocation handler for contextualized bean resolution.
     */
    private static class SpringInvocationHandler<T, VH extends ValueHolder> extends AbstractInvocationHandler<T, VH> {

        private final ApplicationContext applicationContext;
        private final Map<Object, Map<Class<T>, Object>> beanInstanceCache = new ConcurrentHashMap<>();
        private final String beanNamePrefix;

        public SpringInvocationHandler(
                @NonNull final Class<T> targetClass,
                @NonNull final Class<VH> valueHolderClass,
                @NonNull final ApplicationContext applicationContext,
                @NonNull final String beanNamePrefix
        ) {
            super(targetClass, valueHolderClass);
            this.applicationContext = applicationContext;
            this.beanNamePrefix = beanNamePrefix;
        }

        @Override
        protected Object getInstance(@NonNull final Object discriminatorKey) {
            return beanInstanceCache
                    .computeIfAbsent(discriminatorKey, key -> new WeakHashMap<>())
                    .computeIfAbsent(targetClass, this::resolveBeanInstance);
        }

        private Object resolveBeanInstance(@NonNull final Class<T> beanType) {
            return applicationContext.getBeansOfType(beanType).entrySet().stream()
                    .filter(entry -> entry.getKey().startsWith(beanNamePrefix) && isMatchingDiscriminator(entry.getKey()))
                    .map(Map.Entry::getValue)
                    .findFirst()
                    .orElseThrow(() -> new ProxyInstanceNotFoundException("No matching bean found!"));
        }

        private boolean isMatchingDiscriminator(@NonNull final String beanName) {
            return (boolean) invokeStaticMethod(getStaticMethod(IS_MATCHING_DISCRIMINATOR_METHOD, String.class), beanName);
        }
    }

    /**
     * Custom exception thrown when no proxy instance is found.
     */
    public static class ProxyInstanceNotFoundException extends RuntimeException {
        public ProxyInstanceNotFoundException(String message) {
            super(message);
        }

        public ProxyInstanceNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}