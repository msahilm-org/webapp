package com.sahil.healthcheck;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sahil.healthcheck.controller.HealthCheckController;
import com.sahil.healthcheck.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.security.RunAs;

@RunWith(MockitoJUnitRunner.class)
public class HealthCheckControllerTest {


    private MockMvc mockMvc;
    ObjectMapper objMapper = new ObjectMapper();
    ObjectWriter objWriter = objMapper.writer();

    @InjectMocks
    private HealthCheckController healthController;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);

        this.mockMvc= MockMvcBuilders.standaloneSetup(healthController).build();
    }

    @Test
    public void postApiTest() throws Exception {
        User user = new User();
        user.setPassword("Test");
        user.setUsername("Test");
        user.setLastName("Test");
        user.setFirstName("Test");
        user.setLastName("Test");
        MockHttpServletRequestBuilder mockRequest= MockMvcRequestBuilders.post("/v1/account")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objWriter.writeValueAsString(user));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest());

    }
}


