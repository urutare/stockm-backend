package com.urutare.stockservice.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stockm_selling_invoices")
@DiscriminatorValue("SELLING")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SellingInvoice extends Invoice {

    @Column(name = "customer_name")
    private String customerName;
}
