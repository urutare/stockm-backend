package com.urutare.stockservice.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import com.urutare.stockservice.models.enums.Unit;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Table(name = "stockm_pieces")
public class Piece extends BaseEntity {

    @Id
    @GeneratedValue(generator = "UUID", strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "number_of_pieces")
    private int numberOfPieces;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sub_piece_id")
    private Piece subPiece;

    @ManyToOne
    @JoinColumn(name = "parent_piece_id")
    private Piece parentPiece;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit_class")
    private Unit unitClass;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit")
    private Unit unit;

    @Column(name = "selling_price")
    private Double sellingPrice;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}

