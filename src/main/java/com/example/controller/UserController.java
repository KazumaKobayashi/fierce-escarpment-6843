package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

    @RequestMapping("/users")
    @ResponseBody
    public String users(){
    	return "users.";
    }
}
//外部からの受付　
