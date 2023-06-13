package com.urutare.stockmuser.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.urutare.stockmuser.entity.Branch;

public interface BranchRepository extends JpaRepository<Branch, UUID> {
    List<Branch> findByCompany_Id(UUID companyId);
}
