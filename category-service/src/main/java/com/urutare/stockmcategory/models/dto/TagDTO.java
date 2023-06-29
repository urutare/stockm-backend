package com.urutare.stockmcategory.models.dto;

import com.urutare.stockmcategory.entity.ProductTag;
import lombok.Data;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class TagDTO {
    private UUID id;
    private String name;
    private UUID productId;

    public static TagDTO fromTag(ProductTag tag) {
        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(tag.getId());
        tagDTO.setName(tag.getName());
        tagDTO.setProductId(tag.getProduct().getId());
        return tagDTO;
    }

    public static List<TagDTO> fromTags(List<ProductTag> tags) {
        return tags.stream()
                .map(TagDTO::fromTag)
                .collect(Collectors.toList());
    }
}
