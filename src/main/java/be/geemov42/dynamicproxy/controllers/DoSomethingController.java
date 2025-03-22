package be.geemov42.dynamicproxy.controllers;

import be.geemov42.dynamicproxy.annotations.ColorContext;
import be.geemov42.dynamicproxy.holder.ColorContextHolder;
import be.geemov42.dynamicproxy.services.DoSomethingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class DoSomethingController {

    private final DoSomethingService doSomethingService;

    public DoSomethingController(DoSomethingService doSomethingService) {
        this.doSomethingService = doSomethingService;
    }

    @GetMapping("/hello/first")
    public String sayHelloFirst(@RequestParam(name = "ctx", defaultValue = "CTX_GREEN") final ColorContext colorContext) {

        ColorContextHolder.set(colorContext);
        return doSomethingService.sayHelloFirst("first");
    }

    @GetMapping("/hello/second")
    public String sayHelloSecond(@RequestParam(name = "ctx", defaultValue = "CTX_GREEN") final ColorContext colorContext) {

        ColorContextHolder.set(colorContext);
        return doSomethingService.sayHelloSecond("second");
    }

    @GetMapping("/hello/third")
    public String sayHelloThird(@RequestParam(name = "ctx", defaultValue = "CTX_GREEN") final ColorContext colorContext) {

        ColorContextHolder.set(colorContext);
        return doSomethingService.sayHelloThird("third");
    }
}
