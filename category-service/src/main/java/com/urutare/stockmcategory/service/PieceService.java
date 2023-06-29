package com.urutare.stockmcategory.service;

import com.urutare.stockmcategory.entity.Piece;
import com.urutare.stockmcategory.entity.Unit;
import com.urutare.stockmcategory.repository.PieceRepository;
import com.urutare.stockmcategory.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PieceService {
    private final PieceRepository pieceRepository;
    private final UnitRepository unitRepository;

    @Autowired
    public PieceService(PieceRepository pieceRepository, UnitRepository unitRepository) {
        this.pieceRepository = pieceRepository;
        this.unitRepository = unitRepository;
    }

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

    public Optional<Unit> getUnitById(UUID id) {
        return unitRepository.findById(id);
    }

    public List<Piece> getPiecesByProduct(UUID productId) {
        return pieceRepository.findByProductId(productId);
    }
}
