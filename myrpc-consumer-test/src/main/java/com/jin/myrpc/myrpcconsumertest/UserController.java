package com.jin.myrpc.myrpcconsumertest;

import com.jin.myrpc.api.UserService;
import com.jin.myrpc.spirngboot.annotation.MyReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @MyReference
    UserService userService;

    @GetMapping("/hello")
    public String sayHello(String msg){
        return userService.sayHello(msg);
    }
}
