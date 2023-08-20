package com.urutare.stockservice.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "stockm_expenses")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Expense extends BaseEntity {

    @Id
    @GeneratedValue(generator = "UUID", strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "description")
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_url_id")
    private Image proofURL;

    @Column(name = "stuff_id")
    private UUID stuffId;

    @Column(name = "expense_type")
    private String expenseType;

    @Column(name = "from_invoice")
    private Boolean fromInvoice;
}
