package com.alten.altentest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestEndpoint {

    @GetMapping("/")
    String greeting() {
        return "Hello there!";
    }

}
