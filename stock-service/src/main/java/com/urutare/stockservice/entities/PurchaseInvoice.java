package com.urutare.stockservice.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "stockm_purchase_invoices")
@DiscriminatorValue("PURCHASE")
public class PurchaseInvoice extends Invoice {
    @Column(name = "supplier_name")
    private String supplierName;
}
