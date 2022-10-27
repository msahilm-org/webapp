package com.sahil.webapp.service;

import com.sahil.webapp.model.User;
import com.sahil.webapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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

    public User findUserById(UUID id) {
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
