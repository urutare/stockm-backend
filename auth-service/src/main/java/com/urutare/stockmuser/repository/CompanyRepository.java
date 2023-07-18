package com.urutare.stockmuser.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.urutare.stockmuser.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
    List<Company> findAllByDeletedFalse();

    Company findByIdAndDeletedFalse(UUID companyId);
}
