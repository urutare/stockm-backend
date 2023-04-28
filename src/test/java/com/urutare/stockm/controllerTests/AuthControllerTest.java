package com.urutare.stockm.controllerTests;

import com.urutare.stockm.entity.User;
import com.urutare.stockm.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Test
    public void testSignup() throws Exception {
        // create a mock userMap for the request body
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("email", "test@example.com");
        userMap.put("password", "testpassword");

        // create a mock User object for the response
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFullName("Test User");

        userService.registerUser(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // send the request and verify the response
        String url = "http://localhost:" + port + "/api/auth/signup";

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(userMap, headers);

        Map<String, Object> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class).getBody();
        System.out.println(response.toString());
        assertThat(response, notNullValue());
        assertThat(response.get("id"), notNullValue());
    }

    @Test
    public void testLogin() throws Exception {
        // create a mock userMap for the request body
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("email", "test@example.com");
        userMap.put("password", "testpassword");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(userMap, headers);

        // send the request and verify the response
        String url = "http://localhost:" + port + "/api/auth/login";
        Map<String, Object> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class).getBody();
        System.out.println(response.toString());

        assertThat(response, notNullValue());
        assertThat(response.get("token"), notNullValue());
    }
}