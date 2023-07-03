package com.urutare.stockservice.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@NoArgsConstructor
@Table(name = "invoices")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "invoice_type")
public abstract class Invoice {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "destination_id")
    private Destination destination;

    @Column(name = "on_credit")
    private boolean onCredit;

    @Column(name = "mode_of_payment")
    private String modeOfPayment;

    @Column(name = "total_money_to_pay")
    private double totalMoneyToPay;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "paid_date")
    private Date paidDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "promised_date")
    private Date promisedDate;

}
