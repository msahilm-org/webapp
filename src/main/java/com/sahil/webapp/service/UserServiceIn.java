package com.sahil.webapp.service;

import com.sahil.webapp.model.User;

import java.util.List;

public interface UserServiceIn {
    List<User> findAll();
    User createUser(User user);

    User findUserById(long id);

    User updateUser(User user);

    User findByUsername(String username);
}
