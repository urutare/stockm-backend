package com.urutare.stockmcategory.repository;

import com.urutare.stockmcategory.entity.Piece;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PieceRepository extends JpaRepository<Piece, UUID> {
    // You can define custom query methods or use the ones provided by JpaRepository
}
