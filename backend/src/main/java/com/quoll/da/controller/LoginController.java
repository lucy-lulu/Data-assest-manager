package com.quoll.da.controller;

import com.quoll.da.pojo.JwtUtils;
import com.quoll.da.pojo.Result;
import com.quoll.da.pojo.Teacher;
import com.quoll.da.pojo.User;
import com.quoll.da.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @GetMapping("/teacher") // For testing purposes
    public Result getTeacher(){
        List <Teacher> userList = loginService.getTeacher();
        return Result.success(userList);
    }

    @PostMapping("/login")
    public Result login(String username, String password, Integer role){
        User user = loginService.login(username, password, role);
        if (user.getVerifier() == 0){
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", user.getUserId());
            claims.put("username", user.getUsername());
            claims.put("email", user.getEmail());
            claims.put("role", role);
            String jwt = JwtUtils.generateJwt(claims);
            return Result.success(jwt);
        } else if (user.getVerifier() == 1) {
            return Result.error("Username does not exist");
        } else {
            return Result.error("Password is incorrect");
        }
    }

}
