package com.urutare.stockm.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
@Tag(name = "Test", description = "Test API")
@SecurityRequirement(name = "bearerAuth")
public class TestController {
  @GetMapping("/all")
  @Operation(summary = "Public Content", description = "ALL CAN ACCESS THIS ENDPOINT")
  public String allAccess() {
    return "Public Content.";
  }

  @GetMapping("/user")
  @Operation(summary = "USER Content", description = "ONLY USER CAN ACCESS THIS ENDPOINT")
  @PreAuthorize("hasAuthority('USER')")
  public String userAccess() {
    return "USER Content.";
  }

  @GetMapping("/buyer")
  @Operation(summary = "BUYER Board", description = "ONLY BUYER CAN ACCESS THIS ENDPOINT")
  @PreAuthorize("hasAuthority('BUYER')")
  public String buyerAccess() {
    return "BUYER Board.";
  }

  @GetMapping("/seller")
  @Operation(summary = "SELLER Board", description = "ONLY SELLER CAN ACCESS THIS ENDPOINT")
  @PreAuthorize("hasAuthority('SELLER')")
  public String sellerAccess() {
    return "SELLER Board.";
  }

  @GetMapping("/manager")
  @Operation(summary = "MANAGER Board", description = "ONLY MANAGER CAN ACCESS THIS ENDPOINT")
  @PreAuthorize("hasAuthority('MANAGER')")
  public String managerAccess() {
    return "MANAGER Board.";
  }

  @GetMapping("/admin")
  @Operation(summary = "ADMIN Board", description = "ONLY ADMIN CAN ACCESS THIS ENDPOINT")
  @PreAuthorize("hasRole('ADMIN')")
  public String adminAccess() {
    return "ADMIN Board.";
  }
}
