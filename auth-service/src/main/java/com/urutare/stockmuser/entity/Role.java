package com.urutare.stockmuser.entity;

import com.urutare.stockmuser.models.ERole;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "authentico_roles")
@Data
public class Role extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Enumerated(EnumType.STRING)
  @Column(name = "name")
  private ERole name;

  public Role() {

  }

  public Role(ERole name) {
    this.name = name;
  }

}