package com.urutare.stockm.dto.request;

import com.urutare.stockm.models.ERole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignRoleBody {
    private ERole name;
    private Long userId;
}
