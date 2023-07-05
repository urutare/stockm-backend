package com.urutare.syncservice.models.global;

import lombok.Data;

@Data
public class Role {

    private ERole name;

    public Role() {

    }

    public Role(ERole name) {
        this.name = name;
    }

}
