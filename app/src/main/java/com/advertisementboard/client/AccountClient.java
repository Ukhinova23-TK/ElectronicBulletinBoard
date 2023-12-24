package com.advertisementboard.client;

import com.advertisementboard.data.dto.authentication.AuthenticationRequestDto;
import com.advertisementboard.data.dto.authentication.AuthenticationResponseDto;
import com.advertisementboard.data.dto.authentication.RegistrationRequestDto;
import com.advertisementboard.data.dto.user.UserDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AccountClient {

    @GET("/api/account")
    Call<UserDto> getAccount();

    @POST("/api/account/register")
    Call<AuthenticationResponseDto> register(@Body RegistrationRequestDto request);

    @POST("/api/account/authenticate")
    Call<AuthenticationResponseDto> authenticate(@Body AuthenticationRequestDto request);

}
