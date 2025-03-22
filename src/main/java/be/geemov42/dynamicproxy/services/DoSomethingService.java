package be.geemov42.dynamicproxy.services;

import be.geemov42.dynamicproxy.externals.HttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static be.geemov42.dynamicproxy.annotations.Constants.FIRST_HTTP_CLIENT;
import static be.geemov42.dynamicproxy.annotations.Constants.SECOND_HTTP_CLIENT;
import static be.geemov42.dynamicproxy.annotations.Constants.THIRD_HTTP_CLIENT;

@Slf4j
@Component
public class DoSomethingService {

    private final HttpClient firstHttpClient;
    private final HttpClient secondHttpClient;
    private final HttpClient thirdHttpClient;

    public DoSomethingService(
            @Qualifier(FIRST_HTTP_CLIENT) HttpClient firstHttpClient,
            @Qualifier(SECOND_HTTP_CLIENT) HttpClient secondHttpClient,
            @Qualifier(THIRD_HTTP_CLIENT) HttpClient thirdHttpClient
    ) {
        this.firstHttpClient = firstHttpClient;
        this.secondHttpClient = secondHttpClient;
        this.thirdHttpClient = thirdHttpClient;
    }

    public String sayHelloFirst(final String name) {
        return firstHttpClient.doGet(name);
    }

    public String sayHelloSecond(final String name) {
        return secondHttpClient.doGet(name);
    }

    public String sayHelloThird(final String name) {
        return thirdHttpClient.doGet(name);
    }
}
