package com.urutare.stockservice.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "stockm_items_sold")
@EqualsAndHashCode(callSuper = true)
public abstract class ItemSold extends Item {
    @Column(name = "sold_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date soldAt;

    @Column(name = "gross_profit", nullable = false)
    private BigDecimal grossProfit;

    @Column(name = "selling_invoice_id")
    private String sellingInvoiceId;

    @Column(name = "calculate_taxes")
    private boolean calculateTaxes;

    @Column(name = "type")
    private String type;
}
