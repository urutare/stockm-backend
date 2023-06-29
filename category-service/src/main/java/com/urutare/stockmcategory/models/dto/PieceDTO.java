package com.urutare.stockmcategory.models.dto;

import com.urutare.stockmcategory.entity.Piece;
import lombok.Data;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class PieceDTO {
    private UUID id;
    private int numberOfPieces;
    private PieceDTO parentPiece;
    private UUID unitId;
    private PieceDTO childPiece;

    public static PieceDTO fromPiece(Piece piece) {
        PieceDTO pieceDTO = new PieceDTO();
        pieceDTO.setId(piece.getId());
        pieceDTO.setNumberOfPieces(piece.getNumberOfPieces());
        if (piece.getParentPiece() != null) {
            PieceDTO parentPieceDTO = new PieceDTO();
            parentPieceDTO.setId(piece.getParentPiece().getId());
            pieceDTO.setParentPiece(parentPieceDTO);
        }
        if (piece.getUnit() != null) {
            pieceDTO.setUnitId(piece.getUnit().getId());
        }
        if (piece.getChildPiece() != null) {
            PieceDTO childPieceDTO = new PieceDTO();
            childPieceDTO.setId(piece.getChildPiece().getId());
            pieceDTO.setChildPiece(childPieceDTO);
        }
        return pieceDTO;
    }

    public static List<PieceDTO> mapPiecesToDTOs(List<Piece> pieces) {
        return pieces.stream()
                .map(PieceDTO::fromPiece)
                .collect(Collectors.toList());
    }
}
