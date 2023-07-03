package com.urutare.stockservice.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

import com.urutare.stockservice.models.enums.Unit;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Table(name = "stockm_product_sold")
public class ProductSold extends ItemSold {

    @OneToMany(mappedBy = "productSold", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<QuantityInStock> quantityAffected;

    @Column(name = "total_selling_price")
    private Double totalSellingPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_in_stock_id")
    private ProductInStock productInStock;

    @Column(name = "sell_by_piece")
    private Boolean sellByPiece;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "piece_id")
    private Piece piece;

    @Column(name = "unit_class")
    @Enumerated(EnumType.STRING)
    private Unit unitClass;
}
