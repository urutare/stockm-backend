package com.urutare.stockservice.entities;

import jakarta.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.urutare.stockservice.models.enums.Unit;

@Entity
@Table(name = "items")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "item_type")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected String name;

    @Column(name = "category_id")
    protected UUID categoryId;

    protected String description;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "item_id")
    protected Set<Image> images = new HashSet<>();

    @Column(name = "tax_rate")
    protected double taxRate;

    @ElementCollection
    @CollectionTable(name = "item_tags", joinColumns = @JoinColumn(name = "item_id"))
    @Column(name = "tag_id")
    protected Set<UUID> tagIds = new HashSet<>();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    protected Date createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit_class")
    protected Unit unitClass;

    public void addImage(Image image) {
        images.add(image);
    }

    public void removeImage(Image image) {
        images.remove(image);
    }

    public Set<Image> getImages() {
        return images;
    }
}
