package com.sahil.webapp.controller;

import com.sahil.webapp.model.User;
import com.sahil.webapp.service.UserServiceIn;
import com.sahil.webapp.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/v1")
public class UserController 
    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());
    @Autowired
    private UserServiceIn userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/allUser")
    @ResponseBody
    public List<User> findUsers() {
        List<User> userList = (List<User>) userService.findAll();
        return userList;
    }

    @GetMapping("/account/{accountId}")
    @ResponseBody
    public ResponseEntity getUserById(@RequestHeader("Authorization") String authToken,  @PathVariable String accountId) {
        try {
            Helper helper = new Helper();

            String usernameFromRequestHeader= helper.hashToStringFromRequest(authToken);
            User user = (User) userService.findUserById(Long.parseLong(accountId));
//
            if(user.getUsername().equals(usernameFromRequestHeader)){
                return new ResponseEntity(helper.userToMap(user), HttpStatus.OK);
            }
            return new ResponseEntity("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        catch(Exception e){
            return new ResponseEntity("Forbidden",HttpStatus.FORBIDDEN);

        }
    }

    @PutMapping("/account/{accountId}")
    public ResponseEntity updateUser(@RequestBody User us, @PathVariable String accountId,
                                     @RequestHeader("Authorization") String authToken) {
        try {
            Helper helper = new Helper();

            String usernameFromRequestHeader= helper.hashToStringFromRequest(authToken);
            User userFromDB = (User) userService.findUserById(Long.parseLong(accountId));
            if(userFromDB==null){
                return new ResponseEntity("Forbidden",HttpStatus.FORBIDDEN);
            }

            if(userFromDB.getUsername().equals(usernameFromRequestHeader)){
                if(helper.putRequestCheck(us)){

                User updatedUser= helper.userUpdate(us,userFromDB);
                User user = userService.updateUser(updatedUser);
                LOGGER.info("::::User updation successful::::::");
                return new ResponseEntity("No Content",HttpStatus.NO_CONTENT);

                }else{
                    return new ResponseEntity("Forbidden",HttpStatus.FORBIDDEN);
                }

            }else{
                return new ResponseEntity("Unauthorized",HttpStatus.UNAUTHORIZED);
            }
        }
        catch (Exception e){
            if(e.getMessage().contains("No value present")){
                return new ResponseEntity("Forbidden",HttpStatus.FORBIDDEN);
                  }
            LOGGER.info("Bad request");
            return new ResponseEntity("Bad Request",HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/account")
    public ResponseEntity createUser(@RequestBody User us) {
        try {
            User userDB= userService.findByUsername(us.getUsername());
            if(userDB!=null){
                return new ResponseEntity("Forbidden",HttpStatus.FORBIDDEN);
            }
            Helper helper = new Helper();
            if(helper.postRequestCheck(us)){
                    us.setAccountCreated(new Timestamp(System.currentTimeMillis()));
                    String password= us.getPassword();
                    us.setPassword(passwordEncoder.encode(password));
                    User user = userService.createUser(us);
                    return new ResponseEntity(helper.userToMap(user), HttpStatus.CREATED);

                }
            else{
            return new ResponseEntity("Bad Request", HttpStatus.BAD_REQUEST);
            }

        }
        catch (Exception e){
            LOGGER.info("Bad request, unable to handle. Please check JSON parameters");
            return new ResponseEntity("Bad Request", HttpStatus.BAD_REQUEST);
        }

    }
}
