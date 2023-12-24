package com.advertisementboard.data.dto.advertisement;

import com.advertisementboard.data.dto.category.CategoryDto;
import com.advertisementboard.data.dto.user.UserDto;

import com.advertisementboard.data.enumeration.AdvertisementStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
public class AdvertisementDto {

    private Long id;

    @NonNull
    private String heading;

    private String text;

    private UserDto user;

    @NonNull
    private CategoryDto category;

    private String contacts;

    private String url;

    private AdvertisementStatus status;

}
