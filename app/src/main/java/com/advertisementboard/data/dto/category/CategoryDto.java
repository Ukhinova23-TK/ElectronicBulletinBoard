package com.advertisementboard.data.dto.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
public class CategoryDto {

    private Long id;

    @NonNull
    private String name;

    private String description;

}
