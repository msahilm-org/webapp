package com.sahil.webapp.controller;

import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
public class HealthCheckController {
    private static final Logger LOGGER = Logger.getLogger(HealthCheckController.class.getName());
    public static final StatsDClient statsd = new NonBlockingStatsDClient("csye6225", "localhost", 8125);
    @RequestMapping(value= "/healthz", method = RequestMethod.GET)
    public ResponseEntity healthaChecker(){
        statsd.incrementCounter("/v1/healthz.http.get");
        LOGGER.info("API Call:: Healthcheck");
        return new ResponseEntity(HttpStatus.OK);
        //ResponseEntity.status(HttpStatus.OK).body(result.toString())
    }
}
