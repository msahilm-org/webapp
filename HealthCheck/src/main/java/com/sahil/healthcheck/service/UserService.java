package com.sahil.healthcheck.service;

import com.sahil.healthcheck.model.User;
import com.sahil.healthcheck.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserServiceIn{

    @Autowired
    private UserRepository userRepository;



    @Override
    public List<User> findAll() {

        List<User> userList= (List<User>) userRepository.findAll();

        return userList;
    }


    @Override
    public User createUser(User user) {

       // user.setPassword(user.getPassword());
        return (User) userRepository.save(user);


    }

    public User findUserById(long id) {
        User user= userRepository.findById(id).get();

        return user;
    }
    public User updateUser(User user) {

        return userRepository.save(user);
    }
    public User findByUsername(String username) {

        return userRepository.findUserByUsername(username);
    }
}
