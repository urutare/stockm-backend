package com.urutare.syncservice.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.urutare.syncservice.entities.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
    // Add custom query methods if needed
}
