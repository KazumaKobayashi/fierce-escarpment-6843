package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.service.UserService;

@Controller
public class UserController {
	@Autowired
	public UserService userService;

    @RequestMapping("/users")
    @ResponseBody
    public String users(){
    	return userService.getUsers().getResponseJson();
    }
}
//外部からの受付　
