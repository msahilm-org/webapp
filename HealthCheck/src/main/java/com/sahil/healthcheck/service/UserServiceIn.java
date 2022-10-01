package com.sahil.healthcheck.service;

import com.sahil.healthcheck.model.User;

import java.sql.Timestamp;
import java.util.List;

public interface UserServiceIn {
    List<User> findAll();
    User createUser(User user);

    User findUserById(long id);

    User updateUser(User user);
}
