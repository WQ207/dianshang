package com.example.ecommerce.controller;

import com.example.ecommerce.common.Result;
import com.example.ecommerce.dto.LoginDTO;
import com.example.ecommerce.dto.UserDTO;
import com.example.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginDTO loginDTO) {
        String token = userService.login(loginDTO);
        UserDTO userInfo = userService.getUserInfo(token);
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userInfo", userInfo);
        return Result.success(result);
    }

    @PostMapping("/logout")
    public Result<Void> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            userService.logout(token);
        }
        return Result.success();
    }

    @GetMapping("/info")
    public Result<UserDTO> getUserInfo(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            UserDTO userInfo = userService.getUserInfo(token);
            return Result.success(userInfo);
        }
        return Result.error("未登录");
    }
}