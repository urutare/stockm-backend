package com.urutare.stockmuser.entity;

import lombok.Data;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "authentico_employees")
@Data
public class Employee extends BaseEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @Column(nullable = false, unique = true)
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(max = 20)
    private String contactInformation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @NotBlank
    @Size(max = 50)
    private String position;
}
