package com.urutare.stockmcategory.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "products")
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

    @Column(nullable = false, length = 50)
    private String measurementType;

    @Enumerated(EnumType.STRING)
    private TaxType taxType;

    @Column(nullable = false)
    private Double quantityAlert;

    @Column(unique = true, length = 50)
    private String barCode;

    @Column(nullable = false, length = 500)
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pieces_id")
    private Piece pieces;

    @ManyToMany
    @JoinTable(name = "product_tags", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags;

    @Column(name = "image", length = 255)
    private String image;
}
