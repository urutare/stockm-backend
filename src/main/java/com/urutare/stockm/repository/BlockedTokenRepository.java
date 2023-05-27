package com.urutare.stockm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.urutare.stockm.entity.BlockedToken;

public interface BlockedTokenRepository extends JpaRepository<BlockedToken, Long> {

    BlockedToken findByToken(String token);

}
