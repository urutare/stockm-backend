package com.urutare.stockmuser.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.urutare.stockmuser.utils.MapUtils;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "authentico_users")
@Data
public class User {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(nullable = false, unique = true)
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @Column
    private String oauthProvider;

    @Column
    private String oauthId;

    @Column(nullable = false)
    @Size(max = 120)
    @JsonIgnore
    private String password;

    @NotBlank
    @Size(max = 150)
    private String fullName;

    @Size(max = 13)
    private String phoneNumber;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    @Column
    private String avatar;
    @Column
    private boolean verified;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Company> companies = new HashSet<>();

    public User() {
    }

    public User(String email, String password, String fullName,
            String phoneNumber) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    public User(String username, String email, String password, String fullName,
            String phoneNumber) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String email, String password, String fullName, String phoneNumber, Role role) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.roles.add(role);
    }

    public static User fromMap(Map<String, Object> map) {
        return MapUtils.of().fromMap(map, User.class);
    }

    public Map<String, Object> toMap(boolean simplified) {
        return MapUtils.instance.toMap(this, simplified);
    }
}
