package be.geemov42.dynamicproxy.externals;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpClientImpl implements HttpClient {

    private final String wordToAdd;

    public HttpClientImpl(String wordToAdd) {
        this.wordToAdd = wordToAdd;
    }

    @Override
    public String doGet(String name) {
        return String.format("Hello %s from %s", name, wordToAdd);
    }
}
