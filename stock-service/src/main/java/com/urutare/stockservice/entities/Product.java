package com.urutare.stockservice.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Table(name = "stockm_products")
@PrimaryKeyJoinColumn(name = "id")
public class Product extends Item {
    @Column(name = "barcode")
    private String barcode;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pieces_id")
    private Piece piece;

    @Column(name = "low_quantity")
    private Double lowQuantity;

    @Column(name = "low_stock_alert", columnDefinition = "boolean default false")
    private Boolean lowStockAlert;
}
