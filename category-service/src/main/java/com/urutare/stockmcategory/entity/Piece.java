package com.urutare.stockmcategory.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "pieces")
@Data
public class Piece {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private int numberOfPieces;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_piece_id")
    private Piece parentPiece;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Unit unit;

    @Column(nullable = false)
    private double sellingPrice;

    @OneToOne(mappedBy = "parentPiece", cascade = CascadeType.ALL)
    private Piece childPiece;
}

