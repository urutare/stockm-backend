package com.urutare.stockmuser.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Data
@AllArgsConstructor
class SmsAuthResponse {
    private String access_token;
}

@Service
public class SmsService {
    private final RestTemplate restTemplate;

    @Value("${sms.username}")
    private String username;

    @Value("${sms.secret}")
    private String secret;

    @Value("${sms.api.url}")
    private String apiUrl;

    public SmsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendSms(String to, String message) {
        String url = apiUrl + "/messages/pindo";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(getAccessToken());

        System.out.println("Token: " + getAccessToken());

        String requestBody = "{\"text\":\"" + message + "\",\"to\":\"" + to + "\"}";
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            // Handle error appropriately
            throw new RuntimeException("Failed to send SMS. Status code: " + responseEntity.getStatusCode());
        }
    }

    private String getAccessToken() {
        String url = apiUrl + "/auth/login";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = "{\"username\":\"" + username + "\",\"secret\":\"" + secret + "\"}";
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
        if (!responseEntity.getStatusCode().is2xxSuccessful() || responseEntity.getBody() == null) {
            // Handle error appropriately
            throw new RuntimeException("Failed to obtain access token. HTTP Status: " + responseEntity.getStatusCode());
        }

        // Assuming the response body is a JSON object containing the access token
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseBody = objectMapper.readTree(responseEntity.getBody());
            return responseBody.get("access_token").asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
