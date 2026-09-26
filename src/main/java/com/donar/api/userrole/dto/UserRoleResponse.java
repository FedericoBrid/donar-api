package com.donar.api.userrole.dto;

public record UserRoleResponse(
        Long id,
        Long roleId,
        String roleName,
        Boolean status
) {
}