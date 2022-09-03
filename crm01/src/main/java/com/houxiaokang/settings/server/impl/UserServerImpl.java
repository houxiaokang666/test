package com.houxiaokang.settings.server.impl;

import com.houxiaokang.settings.domain.User;
import com.houxiaokang.settings.mapper.UserMapper;
import com.houxiaokang.settings.server.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("userServer")
public class UserServerImpl implements UserServer {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User queryUserByLoginActAndpsw(Map<String, Object> login) {
        return userMapper.selectUserByLoginAndPsw(login);
    }

    @Override
    public List<User> queryUserAll() {
        return userMapper.selectUserAll();
    }
}
