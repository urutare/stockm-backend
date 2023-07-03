package com.urutare.stockservice.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import com.urutare.stockservice.models.enums.Unit;

@Entity
@Data
@NoArgsConstructor
@Table(name = "product_in_stock")
public class ProductInStock {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "inserted_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date insertedAt;

    @OneToMany(mappedBy = "productInStock", cascade = CascadeType.ALL)
    private Set<QuantityInStock> quantitiesInStock;

    @OneToOne(mappedBy = "productInStock", cascade = CascadeType.ALL)
    private QuantityInStock quantityInStockCombined;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit")
    private Unit unit;

    @JoinColumn(name = "product_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;
}
