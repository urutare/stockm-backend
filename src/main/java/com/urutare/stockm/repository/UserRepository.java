package com.urutare.stockm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.urutare.stockm.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
}
