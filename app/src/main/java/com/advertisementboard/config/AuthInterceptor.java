package com.advertisementboard.config;

import static java.util.Objects.nonNull;

import com.advertisementboard.data.dto.authentication.AuthenticationResponseDto;
import java.io.IOException;

import lombok.RequiredArgsConstructor;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

@RequiredArgsConstructor
public class AuthInterceptor implements Interceptor {

    private final AuthenticationResponseDto token;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder authenticatedRequest = request.newBuilder();
        if(nonNull(token.getToken())){
            authenticatedRequest
                    .header("Authorization", token.getToken()).build();
        }
        return chain.proceed(authenticatedRequest.build());
    }

}
