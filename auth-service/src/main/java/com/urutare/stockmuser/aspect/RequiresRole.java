package com.urutare.stockmuser.aspect;

import com.urutare.stockmuser.models.ERole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresRole {
    ERole[] value(); // Role enum
}
