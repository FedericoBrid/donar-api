package com.donar.api.userrole.service;


import com.donar.api.common.exception.DuplicateResourceException;
import com.donar.api.common.exception.ResourceNotFoundException;
import com.donar.api.role.entity.Role;
import com.donar.api.role.repository.IRoleRepository;
import com.donar.api.user.entity.User;
import com.donar.api.user.repository.IUserRepository;
import com.donar.api.userrole.dto.AssignRoleRequest;
import com.donar.api.userrole.dto.UserRoleResponse;
import com.donar.api.userrole.entity.UserRole;
import com.donar.api.userrole.repository.IUserRoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRoleService {

    private final IUserRoleRepository userRoleRepository;
    private final IRoleRepository roleRepository;
    private final IUserRepository userRepository;

    public List<UserRole> findByUserId(Long userId) {
        return userRoleRepository.findByUser_Id(userId);
    }

    public List<UserRole> findByRoleId(Long roleId) {
        return userRoleRepository.findByRole_Id(roleId);
    }

    public List<UserRole> findByUserIdAndStatusTrue(Long userId) {
        return userRoleRepository.findByUser_IdAndStatusTrue(userId);
    }

    @Transactional
    public void assignRole(Long userId, AssignRoleRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        Role role = roleRepository.findById(request.roleId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role not found")
                );

        List<UserRole> userRoles =
                userRoleRepository.findByUser_Id(userId);

        UserRole existingUserRole = userRoles.stream()
                .filter(userRole ->
                        userRole.getRole().getId().equals(role.getId())
                )
                .findFirst()
                .orElse(null);

        LocalDateTime now = LocalDateTime.now();

        if (existingUserRole != null) {

            if (Boolean.TRUE.equals(existingUserRole.getStatus())) {
                throw new DuplicateResourceException(
                        "The user already has this role assigned"
                );
            }

            existingUserRole.setStatus(true);
            existingUserRole.setUpdatedAt(now);

            userRoleRepository.save(existingUserRole);

            return;
        }

        UserRole userRole = UserRole.builder()
                .user(user)
                .role(role)
                .status(true)
                .createdAt(now)
                .build();

        userRoleRepository.save(userRole);
    }

    @Transactional
    public void deactivateRole(Long userId, Long roleId){
        UserRole userRole = userRoleRepository
                .findByUser_IdAndRole_Id(userId, roleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "The user is not assigned this role"
                        )
                );
        if (!Boolean.TRUE.equals(userRole.getStatus())) {
            throw new ResourceNotFoundException(
                    "The role is already inactive"
            );
        }

        userRole.setStatus(false);
        userRole.setUpdatedAt(LocalDateTime.now());

        userRoleRepository.save(userRole);
    }

    public List<UserRoleResponse> getUserRoles(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        return userRoleRepository
                .findByUser_Id(userId)
                .stream()
                .map(userRole -> new UserRoleResponse(
                        userRole.getId(),
                        userRole.getRole().getId(),
                        userRole.getRole().getName(),
                        userRole.getStatus()
                ))
                .toList();
    }
}
