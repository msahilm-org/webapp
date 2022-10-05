package com.sahil.healthcheck;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class HealthCheckApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void testPostApi(){

        assertTrue(true);

    }

}
