package com.advertisementboard.data.dto.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class RegistrationRequestDto {

    @NonNull
    private String login;

    @NonNull
    private String name;

    @NonNull
    private String password;

}
