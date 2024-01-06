package com.advertisementboard.data.dto.category;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
public class CategoryDto implements Serializable {

    private Long id;

    @NonNull
    private String name;

    private String description;

    @androidx.annotation.NonNull
    @Override
    public String toString() {
        return name;
    }
}
