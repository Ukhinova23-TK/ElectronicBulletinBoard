package com.advertisementboard.data.dto.user;

import com.advertisementboard.data.dto.role.RoleDto;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {

    private String login;

    private String name;

    @JsonIgnore
    private String password;

    private RoleDto role;

}
