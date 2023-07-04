package com.urutare.stockservice.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import com.urutare.stockservice.models.enums.Unit;

@Entity
@Data
@NoArgsConstructor
@Table(name = "stockm_quantity")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "quantity_type")
public abstract class Quantity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "value")
    private Double value;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit")
    private Unit unit;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
}
