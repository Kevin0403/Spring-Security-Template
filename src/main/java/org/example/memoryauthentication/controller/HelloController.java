package org.example.memoryauthentication.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping
    public String hello() {
        return "Hello World";
    }

    @GetMapping("/hello")
    public String hello2() {
        return "Hello World ghfghhfghfgg";
    }

}
