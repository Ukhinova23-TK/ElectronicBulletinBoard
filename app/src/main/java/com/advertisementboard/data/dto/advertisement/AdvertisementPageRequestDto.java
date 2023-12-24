package com.advertisementboard.data.dto.advertisement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
public class AdvertisementPageRequestDto {

    @NonNull
    private Integer page;

    @NonNull
    private Integer pageSize;

    private String sortField;

    private String userFilter;

    private Long categoryFilter;

}