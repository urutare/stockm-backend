package com.urutare.stockservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "stockm_piece_quantity")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PieceQuantity extends Quantity {

    @Column(name = "purchasing_price")
    private Double purchasingPrice;

    @Column(name = "selling_price")
    private Double sellingPrice;

    @Column(name = "piece_id")
    private String pieceId;

    @Column(name = "quantity_in_stock_id")
    private String quantityInStockId;

    @Column(name = "purchased_at")
    private LocalDateTime purchasedAt;

}
