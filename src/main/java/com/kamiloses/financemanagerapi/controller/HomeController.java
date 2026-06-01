package com.kamiloses.financemanagerapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/a")
    public String home() {
        return "forward:/index.html";
    }
}
// todo nie może pustego dodac