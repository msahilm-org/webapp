package com.sahil.healthcheck.controller;

import com.sahil.healthcheck.model.User;
import com.sahil.healthcheck.service.UserServiceIn;
import com.sahil.healthcheck.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/v1")
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

    @GetMapping("/account/{accountId}")
    @ResponseBody
    public ResponseEntity getUserById(@PathVariable String accountId) {
        try {
            Helper helper = new Helper();
            User user = (User) userService.findUserById(Long.parseLong(accountId));
            return new ResponseEntity(helper.userToMap(user), HttpStatus.OK);
        }
        catch(Exception e){
            if(e.getMessage().contains("No value present")){
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/account/{accountId}")
    public ResponseEntity updateUser(@RequestBody User us, @PathVariable String accountId) {
        try {
            Helper helper = new Helper();
            LOGGER.info(":::::::::::Name received: " + us.getFirstName());
            User test = (User) userService.findUserById(Long.parseLong(accountId));
            if(helper.validUsername(us)) {
                us.setAccountUpdated(new Timestamp(System.currentTimeMillis()));
                us.setId(Long.parseLong(accountId));
                us.setPassword(test.getPassword());
                us.setAccountCreated(test.getAccountCreated());
                User user = userService.updateUser(us);

                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }else{
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        }
        catch (Exception e){
            if(e.getMessage().contains("No value present")){
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }
            LOGGER.info("Bad request, unable to handle. Please check JSON parameters");
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/account")
    public ResponseEntity createUser(@RequestBody User us) {
        try {
            Helper helper = new Helper();
            LOGGER.info(":::::::::::Name received: " + us.getFirstName());
            if(helper.validUsername(us)) {
                us.setAccountCreated(new Timestamp(System.currentTimeMillis()));
                User user = userService.createUser(us);

                return new ResponseEntity(helper.userToMap(user), HttpStatus.CREATED);
            }else{
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        }
        catch (Exception e){
            LOGGER.info("Bad request, unable to handle. Please check JSON parameters");
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }
}
