package com.urutare.stockservice.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import com.fasterxml.jackson.core.type.TypeReference;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.urutare.stockservice.models.converter.JsonNodeConverter;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "stockm_image")
public class Image extends BaseEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "url")
    @NotBlank
    @URL
    private String url;

    @Column(name = "description")
    private String description;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;

    @Column(name = "uploaded_by")
    private String uploadedBy;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "attributes", columnDefinition = "jsonb")
    @Convert(converter = JsonNodeConverter.class)
    private JsonNode attributes = new ObjectNode(new ObjectMapper().getNodeFactory());

    // Helper methods to access and modify attributes
    public Map<String, Object> getAttributes() {
        return new ObjectMapper().convertValue(attributes, new TypeReference<Map<String, Object>>() {
        });
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = new ObjectMapper().valueToTree(attributes);
    }
}
