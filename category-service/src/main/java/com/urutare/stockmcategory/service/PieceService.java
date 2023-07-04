package com.urutare.stockmcategory.service;

import com.urutare.stockmcategory.entity.Piece;
import com.urutare.stockmcategory.repository.PieceRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PieceService {
    private final PieceRepository pieceRepository;

    public Optional<Piece> getPieceById(UUID id) {
        return pieceRepository.findById(id);
    }

    public Piece createPiece(Piece piece) {
        return pieceRepository.save(piece);
    }

    public Piece updatePiece(Piece piece) {
        return pieceRepository.save(piece);
    }

    public void deletePiece(Piece piece) {
        pieceRepository.delete(piece);
    }

    public List<Piece> getPiecesByProduct(UUID productId) {
        return pieceRepository.findByProductId(productId);
    }
}
