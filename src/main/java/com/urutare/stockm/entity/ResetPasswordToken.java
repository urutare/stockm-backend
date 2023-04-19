package com.urutare.stockm.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reset_password_tokens")
@Data
@NoArgsConstructor
public class ResetPasswordToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long userId;

    private String token;

    public ResetPasswordToken(long userId, String token) {
        this.userId = userId;
        this.token = token;
    }
}
