package com.advertisementboard.data.dto.advertisement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
public class AdvertisementPageResponseDto {

    @NonNull
    private Long elementsCount;

    @NonNull
    private Integer pageCount;

    @NonNull
    private Integer currentPage;

    @NonNull
    private Integer pageSize;

    private List<AdvertisementDto> advertisements;

}
