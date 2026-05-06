package com.example.ecommerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.ecommerce.dto.LoginDTO;
import com.example.ecommerce.dto.UserDTO;
import com.example.ecommerce.entity.SysUser;
import com.example.ecommerce.mapper.SysUserMapper;
import com.example.ecommerce.service.UserService;
import com.example.ecommerce.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private Map<String, Long> tokenCache = new ConcurrentHashMap<>();

    @Override
    public SysUser findByUsername(String username) {
        return userMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
    }

    @Override
    public String login(LoginDTO loginDTO) {
        SysUser user = findByUsername(loginDTO.getUsername());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        if (user.getStatus() != 1) {
            throw new RuntimeException("用户已禁用");
        }
        String token = jwtUtils.generateToken(user.getId(), user.getUsername());
        tokenCache.put("token:" + token, user.getId());
        return token;
    }

    @Override
    public void logout(String token) {
        tokenCache.remove("token:" + token);
    }

    @Override
    public UserDTO getUserInfo(String token) {
        Long userId = jwtUtils.getUserIdFromToken(token);
        SysUser user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setNickname(user.getNickname());
        userDTO.setAvatar(user.getAvatar());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        userDTO.setStatus(user.getStatus());
        return userDTO;
    }
}
