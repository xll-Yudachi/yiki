package com.yudachi.yiki.doc.manage.controller;

import com.yudachi.yiki.common.code.ResponseCode;
import com.yudachi.yiki.common.exception.CustomizeInfoException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/doc/manage")
public class YikiDocManageController {

    @GetMapping("hello")
    public String hello() throws CustomizeInfoException {
        try {
            int i = 1 / 0;
        }catch (Exception e){
            throw new CustomizeInfoException(ResponseCode.NOTONLYONEREQUEST);
        }
        return "hello";
    }
}
