package com.urutare.stockmuser.entity;

import lombok.Data;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "authentico_branches")
@Data
public class Branch extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 200)
    private String location;

    @NotBlank
    @Size(max = 20)
    private String contactInformation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;
}
