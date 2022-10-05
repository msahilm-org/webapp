package com.sahil.webapp;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sahil.webapp.controller.UserController;
import com.sahil.webapp.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@RunWith(Mockito.class)
@SpringBootTest
public class UserControllerTests {


    private MockMvc mockMvc;
    ObjectMapper objMapper = new ObjectMapper();
    ObjectWriter objWriter = objMapper.writer();

    @InjectMocks
    private UserController healthController;

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


