package com.urutare.stockmcategory.models.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.urutare.stockmcategory.entity.Category;

import lombok.Data;

@Data
public class CategoryDTO {
    private UUID id;
    private String name;
    private String image;
    private UUID parentId;
    private List<CategoryDTO> children = new ArrayList<>();
    private int countChildren;

    public static List<CategoryDTO> mapCategoriesToDTOs(List<Category> categories, int childrenLimit) {
        List<CategoryDTO> dtos = new ArrayList<>();

        for (Category category : categories) {
            CategoryDTO dto = new CategoryDTO();
            dto.setId(category.getId());
            dto.setName(category.getName());
            dto.setParentId(category.getParent() != null ? category.getParent().getId() : null);
            dto.setCountChildren(category.getChildren().size());
            dto.setImage(category.getImage());

            List<CategoryDTO> childDTOs = category.getChildren().stream()
                    .limit(childrenLimit)
                    .map(child -> {
                        CategoryDTO childDTO = new CategoryDTO();
                        childDTO.setId(child.getId());
                        childDTO.setName(child.getName());
                        childDTO.setParentId(child.getParent().getId());
                        childDTO.setCountChildren(child.getChildren().size());
                        childDTO.setImage(child.getImage());
                        return childDTO;
                    })
                    .collect(Collectors.toList());

            dto.setChildren(childDTOs);
            dtos.add(dto);
        }

        return dtos;
    }
}
