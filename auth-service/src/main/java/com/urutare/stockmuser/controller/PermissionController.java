package com.urutare.stockmuser.controller;

import com.urutare.stockmuser.entity.Permission;
import com.urutare.stockmuser.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user-service/permissions")
@AllArgsConstructor
@Tag(name = "Permission", description = "Permission API")
@SecurityRequirement(name = "bearerAuth")
public class PermissionController {
    private final PermissionService permissionService;

    @PostMapping
    @Operation(summary = "Create a new permission", description = "Create a new permission", tags = {"Permission"})
    public ResponseEntity<Permission> createPermission(@RequestParam String permissionName, @RequestHeader("userId") UUID userId) {
        Permission permission = new Permission();
        permission.setName(permissionName);
        permission.setCreatedBy(userId);
        Permission createdPermission = permissionService.createPermission(permission);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPermission);
    }

    @GetMapping("/{permissionId}")
    @Operation(summary = "Get a permission by ID", description = "Get a permission by ID", tags = {"Permission"})
    public ResponseEntity<Permission> getPermissionById(@PathVariable UUID permissionId) {
        Permission permission = permissionService.getPermissionById(permissionId);
        return permission != null ? ResponseEntity.ok(permission) : ResponseEntity.notFound().build();
    }

    @GetMapping
    @Operation(summary = "Get all permissions", description = "Get all permissions", tags = {"Permission"})
    public ResponseEntity<List<Permission>> getAllPermissions() {
        List<Permission> permissions = permissionService.getAllPermissions();
        return ResponseEntity.ok(permissions);
    }

    @PutMapping("/{permissionId}")
    @Operation(summary = "Update a permission", description = "Update a permission", tags = {"Permission"})
    public ResponseEntity<Permission> updatePermission(@PathVariable UUID permissionId, @RequestParam String permissionName, @RequestHeader("userId") UUID userId) {
        Permission permission = new Permission();
        permission.setName(permissionName);
        permission.setUpdatedBy(userId);
        Permission updatedPermission = permissionService.updatePermission(permissionId, permission);
        return ResponseEntity.ok(updatedPermission);
    }

    @DeleteMapping("/{permissionId}")
    @Operation(summary = "Delete a permission", description = "Delete a permission", tags = {"Permission"})
    public ResponseEntity<Void> deletePermission(@PathVariable UUID permissionId) {
        permissionService.deletePermission(permissionId);
        return ResponseEntity.noContent().build();
    }
}
