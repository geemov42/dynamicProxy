package be.geemov42.dynamicproxy.config;

import be.geemov42.dynamicproxy.annotations.ColorContext;
import be.geemov42.dynamicproxy.externals.HttpClient;
import be.geemov42.dynamicproxy.holder.ColorContextHolder;
import be.geemov42.dynamicproxy.utils.DynamicProxyUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.stream.Collectors;

import static be.geemov42.dynamicproxy.annotations.Constants.FIRST_HTTP_CLIENT;
import static be.geemov42.dynamicproxy.annotations.Constants.SECOND_HTTP_CLIENT;
import static be.geemov42.dynamicproxy.annotations.Constants.THIRD_HTTP_CLIENT;
import static be.geemov42.dynamicproxy.config.BlueConfig.THIRD_HTTP_CLIENT_BLUE;
import static be.geemov42.dynamicproxy.config.GreenConfig.THIRD_HTTP_CLIENT_GREEN;

@Configuration
public class DynamicProxyConfig {

    @Bean(FIRST_HTTP_CLIENT)
    public HttpClient firstHttpClient(final Map<String, HttpClient> httpClients) {

        return DynamicProxyUtils.createMapProxy(
                HttpClient.class,
                ColorContextHolder.class,
                populateMapForBeanName(FIRST_HTTP_CLIENT, httpClients)
        );
    }

    public static Map<Object, Object> populateMapForBeanName(final String beanNamePrefix, final Map<String, ?> allBeanMap) {
        return allBeanMap.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(beanNamePrefix))
                .collect(
                        Collectors.toMap(
                                entry -> ColorContext.fromValue(entry.getKey().replace(beanNamePrefix, "")),
                                Map.Entry::getValue
                        )
                );
    }

    @Bean(SECOND_HTTP_CLIENT)
    public HttpClient secondHttpClient(final ApplicationContext appContext) {
        return DynamicProxyUtils.createSpringProxy(HttpClient.class, appContext, SECOND_HTTP_CLIENT, ColorContextHolder.class);
    }

    @Bean(THIRD_HTTP_CLIENT)
    public HttpClient thirdHttpClient(
            @Qualifier(THIRD_HTTP_CLIENT_BLUE) final HttpClient thirdHttpClientBlue,
            @Qualifier(THIRD_HTTP_CLIENT_GREEN) final HttpClient thirdHttpClientGreen
    ) {
        return DynamicProxyUtils.createMapProxy(HttpClient.class, ColorContextHolder.class,
                Map.of(
                        ColorContext.CTX_BLUE, thirdHttpClientBlue,
                        ColorContext.CTX_GREEN, thirdHttpClientGreen
                )
        );
    }
}
