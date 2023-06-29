package com.urutare.stockmcategory.repository;

import com.urutare.stockmcategory.entity.Piece;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PieceRepository extends JpaRepository<Piece, UUID> {
    List<Piece> findByProductId(UUID productId);
}
