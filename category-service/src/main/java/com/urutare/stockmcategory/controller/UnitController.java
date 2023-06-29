package com.urutare.stockmcategory.controller;

import com.urutare.stockmcategory.entity.Unit;
import com.urutare.stockmcategory.exception.NotFoundException;
import com.urutare.stockmcategory.models.request.UnitRequestBody;
import com.urutare.stockmcategory.repository.UnitRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Tag(name = "Units", description = "Units API")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@RequestMapping("/api/v1/category-service/units")
public class UnitController {

    private final UnitRepository unitRepository;

    @GetMapping("/{id}")
    @Operation(summary = "Get unit by id")
    public Unit getUnit(@PathVariable UUID id) {
        return unitRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Unit not found"));
    }

    @GetMapping
    @Operation(summary = "Get all units")
    public List<Unit> getAllUnits() {
        return unitRepository.findAll();
    }

    @PostMapping
    @Operation(summary = "Create unit")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Unit> createUnit(@RequestBody @Valid UnitRequestBody unitRequestBody) {
        Unit unit = new Unit();
        unit.setName(unitRequestBody.getName());
        unit.setSymbol(unitRequestBody.getSymbol());
        unit.setCategory(unitRequestBody.getCategory());
        unit.setConversionFactor(unitRequestBody.getConversionFactor());
        unit.setDescription(unitRequestBody.getDescription());

        Unit createdUnit = unitRepository.save(unit);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdUnit);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update unit")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Unit updateUnit(@PathVariable UUID id, @RequestBody @Valid UnitRequestBody unitRequestBody) {
        Unit unit = unitRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Unit not found"));

        unit.setName(unitRequestBody.getName());
        unit.setSymbol(unitRequestBody.getSymbol());
        unit.setCategory(unitRequestBody.getCategory());
        unit.setConversionFactor(unitRequestBody.getConversionFactor());
        unit.setDescription(unitRequestBody.getDescription());

        return unitRepository.save(unit);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete unit")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteUnit(@PathVariable UUID id) {
        Unit unit = unitRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Unit not found"));

        unitRepository.delete(unit);
        return ResponseEntity.noContent().build();
    }
}
