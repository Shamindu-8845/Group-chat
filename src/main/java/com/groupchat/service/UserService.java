package com.groupchat.service;

import com.groupchat.dao.UserDao;
import com.groupchat.dto.User;

public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean authenticate(String username, String password) {
        User user = userDao.findByUsername(username);
        return user != null && user.getPassword().equals(password);
    }
    //here check the user
    public boolean registerUser(String username, String password) {
        if (userDao.findByUsername(username) != null) {
            return false;
        }
        userDao.save(new User(username, password));
        return true;
    }
}