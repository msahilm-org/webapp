package com.sahil.webapp.service;

import com.sahil.webapp.model.User;

import java.util.List;
import java.util.UUID;

public interface UserServiceIn {
    List<User> findAll();
    User createUser(User user);

    User findUserById(UUID id);

    User updateUser(User user);

    User findByUsername(String username);
}
