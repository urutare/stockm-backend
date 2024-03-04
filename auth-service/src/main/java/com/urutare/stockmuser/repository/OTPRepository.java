package com.urutare.stockmuser.repository;

import com.urutare.stockmuser.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OTPRepository extends JpaRepository<OTP, Long> {
    OTP findByUsername(String username);
}
