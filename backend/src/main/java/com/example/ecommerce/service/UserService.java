package com.example.ecommerce.service;

import com.example.ecommerce.dto.LoginDTO;
import com.example.ecommerce.dto.UserDTO;
import com.example.ecommerce.entity.SysUser;

public interface UserService {
    SysUser findByUsername(String username);
    String login(LoginDTO loginDTO);
    void logout(String token);
    UserDTO getUserInfo(String token);
}