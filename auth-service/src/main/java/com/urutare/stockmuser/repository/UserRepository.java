package com.urutare.stockmuser.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.urutare.stockmuser.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("SELECT u FROM User u WHERE u.email=?1")
    User findByEmailAddress(String emailAddress);

    @Query("SELECT COUNT(u) FROM User u WHERE u.email=?1")
    long getCountByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.isActive = :isActive WHERE u.id = :userId")
    void updateIsActive(@Param("userId") UUID userId, @Param("isActive") boolean isActive);
    @Query("SELECT u.isActive FROM User u WHERE u.id=?1")
    boolean getIsActiveById(UUID id);
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailOrUsername(String email, String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);

}
