package com.urutare.stockservice.repository;

import com.urutare.stockservice.entities.Destination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface DestinationRepository extends JpaRepository<Destination, UUID> {

    @Query("SELECT c FROM Destination c WHERE LOWER(c.name) LIKE LOWER(concat('%', :keyword, '%'))")
    Page<Destination> searchByName(@Param("keyword") String keyword, Pageable pageable);
}
