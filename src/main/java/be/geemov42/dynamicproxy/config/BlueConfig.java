package be.geemov42.dynamicproxy.config;

import be.geemov42.dynamicproxy.externals.HttpClient;
import be.geemov42.dynamicproxy.externals.HttpClientImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static be.geemov42.dynamicproxy.annotations.Constants.FIRST_HTTP_CLIENT;
import static be.geemov42.dynamicproxy.annotations.Constants.BLUE;
import static be.geemov42.dynamicproxy.annotations.Constants.SECOND_HTTP_CLIENT;
import static be.geemov42.dynamicproxy.annotations.Constants.THIRD_HTTP_CLIENT;


@Configuration
public class BlueConfig {

    public static final String FIRST_HTTP_CLIENT_BLUE = FIRST_HTTP_CLIENT + BLUE;
    public static final String SECOND_HTTP_CLIENT_BLUE = SECOND_HTTP_CLIENT + BLUE;
    public static final String THIRD_HTTP_CLIENT_BLUE = THIRD_HTTP_CLIENT + BLUE;

    @Bean(FIRST_HTTP_CLIENT_BLUE)
    public HttpClient firstHttpClient() {
        return new HttpClientImpl(BLUE);
    }

    @Bean(SECOND_HTTP_CLIENT_BLUE)
    public HttpClient secondHttpClient() {
        return new HttpClientImpl(BLUE);
    }

    @Bean(THIRD_HTTP_CLIENT_BLUE)
    public HttpClient thirdHttpClient() {
        return new HttpClientImpl(BLUE);
    }
}
