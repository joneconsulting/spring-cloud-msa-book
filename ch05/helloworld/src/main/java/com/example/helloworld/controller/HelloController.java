package com.example.helloworld.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/helloworld")
public class HelloController {
    Environment env;

    public HelloController(Environment env) {
        this.env = env;
    }

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, World!";
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to the Helloworld service.";
    }

    @GetMapping("/message")
    public String message(@RequestHeader("hello-request") String header) {
        return "Hi, there. The request header message is " + header;
    }

    @GetMapping("/check")
    public String check(HttpServletRequest request) {
        return String.format("Hi, there. Helloworld Service is working on PORT %s."
                , env.getProperty("local.server.port"));
    }
}
