package com.urutare.stockmcategory.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "kategora_products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    private MeasurementType measurementType;

    @Enumerated(EnumType.STRING)
    private TaxType taxType;

    @Column(unique = true, length = 50)
    private String barCode;

    @Column(nullable = false, length = 500)
    private String description;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Piece> pieces = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "product_tags", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<ProductTag> tags;

    @Column(name = "image", length = 255)
    private String image;
}
