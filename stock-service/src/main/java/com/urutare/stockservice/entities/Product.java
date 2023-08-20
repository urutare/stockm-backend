package com.urutare.stockservice.entities;

import com.urutare.stockservice.models.enums.Unit;
import com.urutare.stockservice.models.input.ProductInput;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "stockm_products")
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(generator = "UUID", strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "barcode")
    private String barcode;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pieces_id")
    private Piece piece;

    @Column(name = "low_quantity")
    private Double lowQuantity;

    @Column(name = "low_stock_alert", columnDefinition = "boolean default false")
    private Boolean lowStockAlert;

    @Column(name = "category_id")
    protected UUID categoryId;

    protected String description;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "item_id")
    protected Set<Image> images = new HashSet<>();

    @Column(name = "tax_rate")
    protected Double taxRate;

    @ElementCollection
    @CollectionTable(name = "item_tags", joinColumns = @JoinColumn(name = "item_id"))
    @Column(name = "tag_id")
    protected Set<UUID> tagIds = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "unit_class")
    protected Unit unitClass;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "updated_by")
    private UUID updatedBy;

    public void addImage(Image image) {
        images.add(image);
    }

    public void removeImage(Image image) {
        images.remove(image);
    }

    public Product(ProductInput productInput) {
        this.name = productInput.getName();
        this.categoryId = productInput.getCategoryId();
        this.description = productInput.getDescription();
        this.taxRate = productInput.getTaxRate();
        this.barcode = productInput.getBarcode();
        this.lowQuantity = productInput.getLowQuantity();
        this.lowStockAlert = productInput.getLowStockAlert();
    }

}
