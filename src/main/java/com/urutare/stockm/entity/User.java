package com.urutare.stockm.entity;

import com.urutare.stockm.models.Role;
import com.urutare.stockm.utils.MapUtils;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String fullName;

    private String phoneNumber;

    @Column
    private String gender;
    @Column
    @Enumerated(EnumType.STRING)
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
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String email, String password, String fullName, String phoneNumber, Role role) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public static User fromMap(Map<String, Object> map) {
        return MapUtils.of().fromMap(map, User.class);
    }

    public Map<String, Object> toMap(boolean simplified) {
        return MapUtils.instance.toMap(this, simplified);
    }
}
