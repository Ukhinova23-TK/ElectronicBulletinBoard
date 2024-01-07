package com.advertisementboard.util;

import static java.util.Objects.nonNull;

import com.advertisementboard.data.dto.user.UserDto;
import com.advertisementboard.data.enumeration.UserRole;

public class RoleUtil {

    private RoleUtil() {
        throw new RuntimeException();
    }

    public static boolean isModerator(UserDto user) {
        return nonNull(user.getRole())
                && nonNull(user.getRole().getName())
                && (user.getRole().getName().equals(UserRole.MODERATOR.name())
                || user.getRole().getName().equals(UserRole.ADMINISTRATOR.name()));
    }

    public static boolean isAdministrator(UserDto user) {
        return nonNull(user.getRole())
                && nonNull(user.getRole().getName())
                && user.getRole().getName().equals(UserRole.ADMINISTRATOR.name());
    }

}
