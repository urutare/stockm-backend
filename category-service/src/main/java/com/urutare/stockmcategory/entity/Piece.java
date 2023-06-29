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
    String name;

    @Column(nullable = false)
    private int numberOfPieces;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_piece_id")
    private Piece parentPiece;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id")
    private Unit unit;

    @OneToOne(mappedBy = "parentPiece", cascade = CascadeType.ALL)
    private Piece childPiece;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
}
