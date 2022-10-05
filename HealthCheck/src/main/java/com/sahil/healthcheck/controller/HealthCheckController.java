package com.sahil.healthcheck.controller;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthCheckController {
    @RequestMapping(value= "/healthz", method = RequestMethod.GET)
    public ResponseEntity healthaChecker(){

        return new ResponseEntity(HttpStatus.OK);
        //ResponseEntity.status(HttpStatus.OK).body(result.toString())
    }
}
