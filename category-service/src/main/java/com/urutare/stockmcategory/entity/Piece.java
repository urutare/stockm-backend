package com.urutare.stockmcategory.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

import com.urutare.stockmcategory.models.enums.Unit;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "kategora_pieces", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "parent_piece_id", "product_id" }, name = "unique_product_piece")
})
@Data
public class Piece extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    private Integer numberOfPieces;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_piece_id")
    private Piece parentPiece;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit")
    private Unit unit;

    @OneToOne(mappedBy = "parentPiece", cascade = CascadeType.ALL)
    private Piece childPiece;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
}
