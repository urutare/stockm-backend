package com.urutare.stockmuser.service;

import com.urutare.stockmuser.entity.Permission;
import com.urutare.stockmuser.exception.ResourceNotFoundException;
import com.urutare.stockmuser.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public Permission createPermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    public Permission savePermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    public Permission getPermissionById(UUID permissionId) {
        return permissionRepository.findById(permissionId).orElseThrow(() -> new ResourceNotFoundException("Permission not found with ID: " + permissionId));
    }

    public Permission getPermissionByName(String permissionName) {
        return permissionRepository.findByName(permissionName).orElseThrow(() -> new ResourceNotFoundException("Permission not found with name: " + permissionName));
    }

    public void deletePermission(UUID permissionId) {
        Permission existingPermission = getPermissionById(permissionId);
        if (existingPermission != null) {
            permissionRepository.delete(existingPermission);
        }
    }

    public Permission updatePermission(UUID permissionId, Permission updatedPermission) {
        Permission existingPermission = getPermissionById(permissionId);
        if (existingPermission != null) {
            existingPermission.setName(updatedPermission.getName());
            existingPermission.setUpdatedBy(updatedPermission.getUpdatedBy());
            return permissionRepository.save(existingPermission);
        }
        return null;
    }

    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }
}
