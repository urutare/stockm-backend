package com.urutare.stockmuser.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.urutare.stockmuser.entity.Role;
import com.urutare.stockmuser.models.ERole;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
    Boolean existsByName(String name);

}
