package com.urutare.stockm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.urutare.stockm.entity.UserEntity;
import com.urutare.stockm.repository.UserEntityRepository;

@RestController
@RequestMapping("/api/unstructured")
public class UserEntityController {
    
    @Autowired
    private UserEntityRepository userEntityRepository;
  

    Logger logger = LoggerFactory.getLogger(UserEntityController.class);
    
    @GetMapping("/users")
    public ResponseEntity<Object> getAllUsers() {
        try {
            List<UserEntity> users = userEntityRepository.findAll();
            return ResponseEntity.ok().body(users);
        } catch (Exception e) {
            logger.error("An error occurred while retrieving all users", e);
            Map<String, String> response = new HashMap<>();
            response.put("message", "An error occurred while retrieving all users");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
