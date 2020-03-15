package com.jin.myrpc.myrpcprovidertest;

import com.jin.myrpc.api.UserService;
import com.jin.myrpc.spirngboot.annotation.MyService;
import org.springframework.stereotype.Component;

/**
 * @author wangjin
 */
@Component
@MyService(interfaceClass=UserService.class)
public class UserServiceImpl implements UserService {
    @Override
    public String sayHello(String msg) {
        return "hello:"+msg+"!";
    }
}
