package com.example.apitest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DartWebController {

    @GetMapping({"/", "/dart"})
    public String index() {
        return "dart-data"; // resources/templates/dart-data.html
    }
}
