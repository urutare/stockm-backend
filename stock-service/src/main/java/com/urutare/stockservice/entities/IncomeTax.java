package com.urutare.stockservice.entities;

import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "stockm_income_tax")
public class IncomeTax extends BaseEntity {

    @Id
    @GeneratedValue(generator = "UUID", strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "single_product")
    private Double singleProduct;

    @Column(name = "number_of_product")
    private Double numberOfProduct;
}
