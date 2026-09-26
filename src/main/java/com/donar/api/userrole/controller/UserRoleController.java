package com.donar.api.userrole.controller;

import com.donar.api.userrole.dto.AssignRoleRequest;
import com.donar.api.userrole.dto.UserRoleResponse;
import com.donar.api.userrole.service.UserRoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleService userRoleService;

    @PostMapping("/{userId}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void assignRole(
            @PathVariable Long userId,
            @Valid @RequestBody AssignRoleRequest request
    ) {
        userRoleService.assignRole(userId, request);
    }

    @PutMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeRole(
            @PathVariable Long userId,
            @PathVariable Long roleId
    ) {
        userRoleService.deactivateRole(userId, roleId);
    }

    @GetMapping("/{userId}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserRoleResponse> getUserRoles(@PathVariable Long userId){
        return userRoleService.getUserRoles(userId);
    }
}