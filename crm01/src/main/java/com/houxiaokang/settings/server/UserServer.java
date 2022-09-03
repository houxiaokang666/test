package com.houxiaokang.settings.server;

import com.houxiaokang.settings.domain.User;

import java.util.List;
import java.util.Map;

public interface UserServer {
    User queryUserByLoginActAndpsw(Map<String, Object> login);

    List<User> queryUserAll();
}
