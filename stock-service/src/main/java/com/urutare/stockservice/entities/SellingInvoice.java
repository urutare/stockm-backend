package com.urutare.stockservice.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "stockm_selling_invoices")
@DiscriminatorValue("SELLING")
public class SellingInvoice extends Invoice {

    @Column(name = "customer_name")
    private String customerName;
}
