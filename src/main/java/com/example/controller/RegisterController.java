package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.service.UserService;

@Controller
public class RegisterController {
	@Autowired
	private UserService userService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public String register(
    		@RequestParam("name") String username,
    		@RequestParam("passwd") String password) {

        return userService.regist(username, password).getResponseJson();
    }
}
//外部からの受付　
