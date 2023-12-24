package com.advertisementboard.config;

import static java.util.Objects.isNull;

import com.advertisementboard.client.AccountClient;
import com.advertisementboard.data.dto.authentication.AuthenticationResponseDto;
import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppConfiguration {

    private static AuthenticationResponseDto token;

    private static Retrofit retrofit;

    private static AuthInterceptor authInterceptor;

    private static AccountClient accountClient;

    private static final String BASE_URL = "http://192.168.0.102:8080/";

    public static AuthenticationResponseDto token() {
        if(isNull(token)) {
            token = new AuthenticationResponseDto();
        }
        return token;
    }

    public static Retrofit retrofit() {
        if(isNull(retrofit)) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(authInterceptor())
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(new Gson()))
                    .client(client)
                    .build();
        }
        return retrofit;
    }

    public static AuthInterceptor authInterceptor() {
        if(isNull(authInterceptor)) {
            authInterceptor = new AuthInterceptor(token());
        }
        return authInterceptor;
    }

    public static AccountClient accountClient() {
        if(isNull(accountClient)) {
            accountClient = retrofit().create(AccountClient.class);
        }
        return accountClient;
    }

}
