package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GroupController {

    @RequestMapping("/group/create")
   	@ResponseBody
   	public String create(){
    	return "create.";
    }
    @RequestMapping("/group/{groupId}/find")
    @ResponseBody
    public String find(@PathVariable("groupId") String groupId){
    	return "find.";
    }
	@RequestMapping("/group/{groupId}/info")
	@ResponseBody
	public String info(@PathVariable("groupId") String groupId){
		return "info";
	}
}
//外部からの受付　
