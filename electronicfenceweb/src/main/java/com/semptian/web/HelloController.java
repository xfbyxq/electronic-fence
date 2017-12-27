package com.semptian.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/hello",produces = "Application/json")
public class HelloController {

    @RequestMapping(value = "/world.json",method = RequestMethod.GET)
    public Object sayWorld(){
        return "HelloWorld!!";
    }
}
