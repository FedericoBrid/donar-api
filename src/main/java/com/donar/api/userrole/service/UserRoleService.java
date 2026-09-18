package com.donar.api.userrole.service;


import com.donar.api.userrole.entity.UserRole;
import com.donar.api.userrole.repository.IUserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRoleService {

    private final IUserRoleRepository userRoleRepository;

    public List<UserRole> findByUserId(Long userId) {
        return userRoleRepository.findByUser_Id(userId);
    }

    public List<UserRole> findByRoleId(Long roleId) {
        return userRoleRepository.findByRole_Id(roleId);
    }

    public List<UserRole> findByUserIdAndStatusTrue(Long userId) {
        return userRoleRepository.findByUser_IdAndStatusTrue(userId);
    }
}
