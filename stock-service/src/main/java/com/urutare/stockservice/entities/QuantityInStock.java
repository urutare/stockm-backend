package com.urutare.stockservice.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Table(name = "stockm_quantity_in_stock")
public class QuantityInStock extends Quantity {
    @Column(name = "purchasing_price")
    private Double purchasingPrice;

    @Column(name = "selling_price")
    private Double sellingPrice;

    @Column(name = "purchased_at")
    private Date purchasedAt;
}
