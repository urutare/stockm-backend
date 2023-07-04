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

@Entity
@Table(name = "stockm_items")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "item_type")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Item extends BaseEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

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
