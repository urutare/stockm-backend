package com.urutare.stockmuser.repository;

import com.urutare.stockmuser.entity.UserSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserSettingRepository extends JpaRepository<UserSetting, UUID> {
    UserSetting findByUserId(UUID userId);
}
