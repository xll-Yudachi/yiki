package com.yudachi.yiki.doc.manage.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/doc/manage")
public class YikiDocManageController {

    @GetMapping("hello")
    public String hello(){
        int i = 1 / 0;
        return "hello";
    }
}
