package com.karakoc.sofra.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {
    @GetMapping("/test-endpoint")
    public String hello(){
        return "HELLO WORLD!";
    }
}
