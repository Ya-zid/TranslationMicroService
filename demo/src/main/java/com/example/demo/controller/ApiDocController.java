package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class ApiDocController {

    @GetMapping("/")
    public RedirectView redirectToSwaggerUI() {
        return new RedirectView("/swagger-ui.html");
    }
}