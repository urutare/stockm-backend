package com.urutare.stockmuser.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "authentico_subscription_features")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionFeature extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100, unique = true)
    private String name;

    @NotBlank
    @Size(max = 200)
    @Column(length = 200)
    private String description;

    @JsonIgnore
    @ManyToMany(mappedBy = "features")
    private Set<Subscription> subscriptionPlans = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubscriptionFeature that = (SubscriptionFeature) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
