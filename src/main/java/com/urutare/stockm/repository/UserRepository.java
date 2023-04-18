package com.urutare.stockm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.urutare.stockm.dto.UserDto;
import com.urutare.stockm.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findById(String userId);

    @Query("SELECT u FROM User u WHERE u.email=?1")
    User findByEmailAddress(String emailAddress);

    @Query("SELECT COUNT(u) FROM User u WHERE u.email=?1")
    long getCountByEmail(String email);

    @Query("SELECT new com.urutare.stockm.dto.UserDto(u) FROM User u WHERE u.id=?1")
    UserDto findUserDtoById(String id);

}
