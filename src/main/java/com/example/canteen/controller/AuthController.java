package com.example.canteen.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @GetMapping("/")
    public String greet(HttpServletRequest request) {
        return "Welcome to  "+request.getSession().getId();
    }

}