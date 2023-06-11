package com.urutare.stockmuser.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.urutare.stockmuser.entity.ResetPasswordToken;

public interface ResetRepository extends JpaRepository<ResetPasswordToken, Long> {
    ResetPasswordToken findByToken(String token);
}
