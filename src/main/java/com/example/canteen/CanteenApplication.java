package com.example.canteen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.GetMapping;

@EnableCaching
@SpringBootApplication
public class CanteenApplication {
    @GetMapping("/hello")
    public String getGreeting() {
        return "Hello world.";
    }
    public static void main(String[] args) {

        SpringApplication.run(CanteenApplication.class, args);

    }

}
