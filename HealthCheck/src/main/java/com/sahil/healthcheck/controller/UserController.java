package com.sahil.healthcheck.controller;

import com.sahil.healthcheck.model.User;
import com.sahil.healthcheck.service.UserServiceIn;
import com.sahil.healthcheck.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
public class UserController {
    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());
    @Autowired
    private UserServiceIn userService;


    @GetMapping("/allUser")
    @ResponseBody
    public List<User> findUsers() {

        List<User> userList = (List<User>) userService.findAll();



        return userList;
    }

    @PostMapping("/v1/account")
    public ResponseEntity createUser(@RequestBody User us) {

        LOGGER.info(":::::::::::Name received: "+ us.getFirstName());
        us.setAccountCreated(new Timestamp(System.currentTimeMillis()));
        User user= userService.createUser(us);
        Helper helper= new Helper();
        return new ResponseEntity(helper.userToMap(user), HttpStatus.OK);

    }
}
