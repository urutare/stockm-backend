package com.urutare.stockservice.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stockm_purchase_invoices")
@DiscriminatorValue("PURCHASE")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PurchaseInvoice extends Invoice {
    @Column(name = "supplier_name")
    private String supplierName;
}
