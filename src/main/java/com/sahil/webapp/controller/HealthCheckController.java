package com.sahil.webapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    @RequestMapping(value= "/healthz", method = RequestMethod.GET)
    public ResponseEntity healthaChecker(){

        return new ResponseEntity(HttpStatus.OK);
        //ResponseEntity.status(HttpStatus.OK).body(result.toString())
    }
}
