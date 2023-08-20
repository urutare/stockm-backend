package com.urutare.stockservice.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import com.urutare.stockservice.models.enums.Unit;

@Entity
@Data
@NoArgsConstructor
@Table(name = "stockm_product_purchase")
public class ProductPurchase {

    @Id
    @GeneratedValue(generator = "UUID", strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "quantity_purchase_id")
    private Quantity quantityPurchase;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "purchase_date")
    private Date purchaseDate;

    @Column(name = "purchasing_unit_price")
    private Double purchasingUnitPrice;

    @Column(name = "purchase_total_price")
    private double purchaseTotalPrice;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expired_date")
    private Date expiredDate;

    @Column(name = "purchase_product_id")
    private String purchaseProductId;

    @Column(name = "quantity_in_stock_ref_id")
    private String quantityInStockRefId;

    @Column(name = "purchase_invoice_ref_id")
    private String purchaseInvoiceRefId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "product_id", insertable = false, updatable = false)
    private String productId;

    @Column(name = "purchase_by_piece")
    private boolean purchaseByPiece;

    @Column(name = "piece_id")
    private String pieceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit_class")
    private Unit unitClass;
}
