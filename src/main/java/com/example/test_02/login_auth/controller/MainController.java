package com.example.test_02.login_auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // templates/login.html
    }

    @GetMapping("/loginSuccess")
    public String loginSuccessPage() {
        return "loginSuccess"; // templates/loginSuccess.html
    }
}
