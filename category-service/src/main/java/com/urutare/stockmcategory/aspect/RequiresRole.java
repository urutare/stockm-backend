package com.urutare.stockmcategory.aspect;

import com.urutare.stockmcategory.models.enums.UserRole;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresRole {
    UserRole[] value(); // Role enum
}
