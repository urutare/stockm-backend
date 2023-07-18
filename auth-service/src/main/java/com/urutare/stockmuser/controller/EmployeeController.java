package com.urutare.stockmuser.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.urutare.stockmuser.dto.EmployeeDTO;
import com.urutare.stockmuser.entity.Employee;
import com.urutare.stockmuser.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/user-service/employees")
@AllArgsConstructor
@Tag(name = "Employee", description = "Employee API")
@SecurityRequirement(name = "bearerAuth")
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping
    @Operation(summary = "Create a new employee", description = "Create a new employee", tags = { "Employee" })
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee createdEmployee = employeeService.createEmployee(employeeDTO);
        EmployeeDTO responseDTO = new EmployeeDTO(createdEmployee);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/{employeeId}")
    @Operation(summary = "Get an employee by ID", description = "Get an employee by ID", tags = { "Employee" })
    public ResponseEntity<Employee> getEmployeeById(@PathVariable UUID employeeId) {
        Employee employee = employeeService.getEmployeeById(employeeId);
        return employee != null ? ResponseEntity.ok(employee) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{employeeId}")
    @Operation(summary = "Update an employee", description = "Update an employee", tags = { "Employee" })
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable UUID employeeId,
            @RequestBody EmployeeDTO employeeDTO) {
        Employee employee = employeeService.updateEmployee(employeeId, employeeDTO);
        EmployeeDTO responseDTO = new EmployeeDTO(employee);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{employeeId}")
    @Operation(summary = "Delete an employee", description = "Delete an employee", tags = { "Employee" })
    public ResponseEntity<Void> deleteEmployee(@PathVariable UUID employeeId) {
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search for employees", description = "Search for employees based on criteria", tags = {
            "Employee" })
    public ResponseEntity<List<EmployeeDTO>> searchEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String position) {
        List<Employee> employees = employeeService.searchEmployees(name, email, position);
        List<EmployeeDTO> employeeDTOs = employees.stream()
                .map(EmployeeDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(employeeDTOs);
    }

    @GetMapping("/branch/{branchId}")
    @Operation(summary = "Get all employees in a branch", description = "Get all employees in a branch", tags = {
            "Employee" })
    public ResponseEntity<List<EmployeeDTO>> getAllEmployeesByBranch(@PathVariable UUID branchId) {
        List<Employee> employees = employeeService.getAllEmployeesByBranch(branchId);
        List<EmployeeDTO> employeeDTOs = employees.stream()
                .map(EmployeeDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(employeeDTOs);
    }

}
