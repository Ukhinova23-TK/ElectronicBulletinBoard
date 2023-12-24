package com.advertisementboard.client;

import com.advertisementboard.data.dto.advertisement.AdvertisementDto;
import com.advertisementboard.data.dto.advertisement.AdvertisementPageRequestDto;
import com.advertisementboard.data.dto.advertisement.AdvertisementPageResponseDto;
import com.advertisementboard.data.dto.advertisement.AdvertisementRequestDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AdvertisementClient {

    @GET("/api/advertisements")
    Call<List<AdvertisementDto>> getAdvertisements();

    @GET("/api/advertisements/{id}")
    Call<AdvertisementDto> getAdvertisement(@Path("id") long id);

    @POST("/api/advertisements/filter")
    Call<AdvertisementPageResponseDto> getAdvertisementsByFilter(
            @Body AdvertisementPageRequestDto request
    );

    @POST("/api/advertisements")
    Call<Long> createAdvertisement(@Body AdvertisementRequestDto request);

    @PUT("/api/advertisements")
    Call<?> updateAdvertisement(@Body AdvertisementRequestDto request);

    @DELETE("/api/advertisements/{id}")
    Call<?> deleteAdvertisement(@Path("id") long id);

    @PUT("/api/advertisements/{id}/reject")
    Call<?> rejectAdvertisement(@Path("id") long id);

    @PUT("/api/advertisements/{id}/confirm")
    Call<?> confirmAdvertisement(@Path("id") long id);

}
