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
public class AdvertisementRequestDto {

    private Long id;

    @NonNull
    private String heading;

    private String text;

    @NonNull
    private Long categoryId;

    private String contacts;

    private String url;

}
