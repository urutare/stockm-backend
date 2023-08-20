package com.urutare.stockservice.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import com.urutare.stockservice.models.enums.Unit;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public abstract class Item extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "category_id")
    private UUID categoryId;

    private String description;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "item_id")
    private Set<Image> images = new HashSet<>();

    @Column(name = "tax_rate")
    private double taxRate;

    @ElementCollection
    @CollectionTable(name = "item_tags", joinColumns = @JoinColumn(name = "item_id"))
    @Column(name = "tag_id")
    private Set<UUID> tagIds = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "unit_class")
    private Unit unitClass;

    public void addImage(Image image) {
        images.add(image);
    }

    public void removeImage(Image image) {
        images.remove(image);
    }

}
