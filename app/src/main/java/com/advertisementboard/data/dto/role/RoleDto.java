package com.advertisementboard.data.dto.role;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleDto implements Serializable {

    private Integer id;

    private String name;

}
