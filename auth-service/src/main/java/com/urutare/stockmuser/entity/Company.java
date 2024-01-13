package com.urutare.stockmuser.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "authentico_companies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank
    @Size(max = 200)
    @Column(nullable = false, length = 200)
    private String address;

    @Email
    @Size(max = 100)
    @Column(nullable = true, length = 100)
    private String email;

    @Size(max = 255)
    private String logo;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String tin;

    @NotBlank
    @Size(max = 100)
    @Column(length = 100)
    private String website;

    @NotBlank
    @Size(max = 100)
    @Column(length = 13)
    private String phone;


    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private boolean deleted;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
