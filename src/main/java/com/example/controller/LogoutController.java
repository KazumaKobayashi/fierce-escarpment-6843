package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LogoutController {

    @RequestMapping("/logout")
    @ResponseBody
    public String logout(){
    	return "logout.";
    }
}
//外部からの受付　
