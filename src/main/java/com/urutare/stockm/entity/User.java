package com.urutare.stockm.entity;

import com.urutare.stockm.models.Role;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String phoneNumber;

    @Column
    private String gender;
    @Column
    private Role role;
    @Column
    private boolean isEnabled;
    /**
     * for chat purpose
     */
    @Column
    private boolean isActive;
    @Column
    private String avatar;
    @Column
    private boolean verified;
    @Column
    private Date lastTimePasswordUpdated;
    @Column
    private String birthdate;
    //    private List<String> Permissions;
    @Column
    private Date lastLogin;


    public User() {
    }

    public User(String email, String password, String fullName,
                String phoneNumber) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
