package be.geemov42.dynamicproxy.config;

import be.geemov42.dynamicproxy.externals.HttpClient;
import be.geemov42.dynamicproxy.externals.HttpClientImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static be.geemov42.dynamicproxy.annotations.Constants.FIRST_HTTP_CLIENT;
import static be.geemov42.dynamicproxy.annotations.Constants.GREEN;
import static be.geemov42.dynamicproxy.annotations.Constants.SECOND_HTTP_CLIENT;
import static be.geemov42.dynamicproxy.annotations.Constants.THIRD_HTTP_CLIENT;

@Configuration
public class GreenConfig {

    public static final String FIRST_HTTP_CLIENT_GREEN = FIRST_HTTP_CLIENT + GREEN;
    public static final String SECOND_HTTP_CLIENT_GREEN = SECOND_HTTP_CLIENT + GREEN;
    public static final String THIRD_HTTP_CLIENT_GREEN = THIRD_HTTP_CLIENT + GREEN;

    @Bean(FIRST_HTTP_CLIENT_GREEN)
    public HttpClient firstHttpClient() {
        return new HttpClientImpl(GREEN);
    }

    @Bean(SECOND_HTTP_CLIENT_GREEN)
    public HttpClient secondHttpClient() {
        return new HttpClientImpl(GREEN);
    }

    @Bean(THIRD_HTTP_CLIENT_GREEN)
    public HttpClient thirdHttpClient() {
        return new HttpClientImpl(GREEN);
    }
}
