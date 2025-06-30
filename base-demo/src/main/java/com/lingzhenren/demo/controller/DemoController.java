package com.lingzhenren.demo.controller;

import com.lingzhenren.demo.entity.User;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @description: 对该类作用进行描述
 * @author: yanghaitao
 * @date: 2025/6/9
 */
@RequestMapping("/demo")
@RestController
public class DemoController {

    @PostMapping("/test")
    public String test(@RequestBody User user){
        System.out.println(user);
        return "test";
    }

    @GetMapping("/user")
    public User user(){
        User user = new User().setName("user").setNow(new Date());

        System.out.println(user);
        return user;
    }
}
