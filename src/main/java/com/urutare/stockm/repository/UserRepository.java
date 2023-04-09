package com.urutare.stockm.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.urutare.stockm.entity.UserEntity;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {
    
}
