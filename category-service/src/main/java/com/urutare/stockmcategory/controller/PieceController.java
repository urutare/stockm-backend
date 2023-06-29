package com.urutare.stockmcategory.controller;

import java.util.UUID;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.urutare.stockmcategory.entity.Piece;
import com.urutare.stockmcategory.exception.NotFoundException;
import com.urutare.stockmcategory.models.dto.PieceDTO;
import com.urutare.stockmcategory.models.request.PieceRequestBody;
import com.urutare.stockmcategory.service.PieceService;
import com.urutare.stockmcategory.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/category-service/pieces")
@Tag(name = "Pieces", description = "Pieces API")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class PieceController {

    private final PieceService pieceService;
    private final ProductService productService;

    @GetMapping("/{id}")
    @Operation(summary = "Get piece by id")
    public PieceDTO getPiece(@PathVariable UUID id) {
        Piece piece = pieceService.getPieceById(id)
                .orElseThrow(() -> new NotFoundException("Piece not found"));

        return PieceDTO.fromPiece(piece);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Create piece")
    public ResponseEntity<PieceDTO> createPiece(@RequestBody @Valid PieceRequestBody pieceBody) {
        Piece piece = new Piece();
        piece.setNumberOfPieces(pieceBody.getNumberOfPieces());
        piece.setProduct(productService.getProductById(pieceBody.getProductId()).orElse(null));
        piece.setUnit(pieceService.getUnitById(pieceBody.getUnitId()).orElse(null));

        Piece createdPiece = pieceService.createPiece(piece);
        return ResponseEntity.status(HttpStatus.CREATED).body(PieceDTO.fromPiece(createdPiece));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Update piece")
    public PieceDTO updatePiece(@PathVariable UUID id, @RequestBody @Valid PieceRequestBody pieceBody) {
        Piece piece = pieceService.getPieceById(id)
                .orElseThrow(() -> new NotFoundException("Piece not found"));

        piece.setNumberOfPieces(pieceBody.getNumberOfPieces());
        piece.setProduct(productService.getProductById(pieceBody.getProductId()).orElse(null));
        piece.setUnit(pieceService.getUnitById(pieceBody.getUnitId()).orElse(null));

        Piece updatedPiece = pieceService.updatePiece(piece);
        return PieceDTO.fromPiece(updatedPiece);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Delete piece")
    public ResponseEntity<Void> deletePiece(@PathVariable UUID id) {
        Piece piece = pieceService.getPieceById(id)
                .orElseThrow(() -> new NotFoundException("Piece not found"));

        pieceService.deletePiece(piece);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get pieces by product ID")
    public List<PieceDTO> getPiecesByProduct(@PathVariable UUID productId) {
        List<Piece> pieces = pieceService.getPiecesByProduct(productId);
        return PieceDTO.mapPiecesToDTOs(pieces);
    }
}
