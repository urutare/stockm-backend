package com.urutare.stockmuser.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.urutare.stockmuser.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    List<Employee> findByBranchId(UUID branchId);

    @Query("SELECT e FROM Employee e WHERE " +
            "(:name IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:email IS NULL OR LOWER(e.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
            "(:position IS NULL OR LOWER(e.position) LIKE LOWER(CONCAT('%', :position, '%')))")
    List<Employee> searchEmployees(@Param("name") String name, @Param("email") String email,
            @Param("position") String position);

}