package com.urutare.stockm.controller;

import com.urutare.stockm.dto.request.AddRoleBody;
import com.urutare.stockm.dto.request.AssignRoleBody;
import com.urutare.stockm.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@Tag(name = "Roles")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasAuthority('ADMIN')")
public class RoleController {

    private final UserService userService;

    @PostMapping("Role/add-role")
    @Operation(summary = "Add role", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Object> addRole( @RequestBody @Validated AddRoleBody roleBody){
        userService.CreateRole(roleBody);
        return ResponseEntity.ok().body("{\"message\": \"role created successfully\"}");
    }
    @PostMapping("Role/assign-role")
    @Operation(summary = "Assign role", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Object> assignRole(@RequestBody @Validated AssignRoleBody roleBody){
        userService.assignRole(roleBody);
        return ResponseEntity.ok().body("{\"message\": \"role assigned successfully\"}");
    }
}
