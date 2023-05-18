package com.urutare.stockm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.urutare.stockm.entity.Role;
import com.urutare.stockm.models.ERole;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
