package com.sahil.healthcheck.util;

import com.sahil.healthcheck.model.User;

import java.util.HashMap;
import java.util.Map;

public class Helper {

    public Map userToMap(User user){
        Map<String, String> map= new HashMap<String, String>();
        map.put("id", String.valueOf(user.getId()));
        map.put("first_name", user.getFirstName());
        map.put("last_name", user.getLastName());
        map.put("username", user.getUsername());
        map.put("account_created", user.getAccountCreated().toString());
        map.put("account_updated", user.getAccountUpdated()==null?  "null":  user.getAccountUpdated().toString());
        return map;
    }
}
