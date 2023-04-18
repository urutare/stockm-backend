package com.urutare.stockm.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.urutare.stockm.controller.AuthController;
import com.urutare.stockm.entity.User;
import com.urutare.stockm.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testLogin() throws Exception {
        // create a mock UserMap for the request body
        Map<String, Object> UserMap = new HashMap<>();
        UserMap.put("email", "test@example.com");
        UserMap.put("password", "testpassword");

        // create a mock User object for the response
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("testpassword");

        // mock the userService method to return the mock User object
        when(userService.validateUser(anyString(), anyString())).thenReturn(user);

        // send the request and verify the response
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(UserMap)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()));
    }
}
