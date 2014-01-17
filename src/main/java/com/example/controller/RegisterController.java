package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RegisterController {
    @RequestMapping("/register")
    @ResponseBody
    public String register() {
        return "register";
    }
}
//外部からの受付　
