package com.groupchat.dao;

import com.groupchat.dto.User;
import java.util.HashMap;
import java.util.Map;

public class UserDao {
    private final Map<String, User> users = new HashMap<>();

    public User findByUsername(String username) {
        return users.get(username);
    }

    public void save(User user) {
        users.put(user.getUsername(), user);
    }
}