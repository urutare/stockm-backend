package com.urutare.stockmuser.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.urutare.stockmuser.entity.BlockedToken;

public interface BlockedTokenRepository extends JpaRepository<BlockedToken, Long> {

    BlockedToken findByToken(String token);

}
