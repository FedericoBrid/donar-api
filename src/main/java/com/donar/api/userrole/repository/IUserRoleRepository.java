package com.donar.api.userrole.repository;

import com.donar.api.userrole.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IUserRoleRepository extends JpaRepository<UserRole, Long> {

    List<UserRole> findByUser_Id(Long userId);
    List<UserRole> findByRole_Id(Long roleId);
    List<UserRole> findByUser_IdAndStatusTrue(Long userId);
    Optional<UserRole> findByUser_IdAndRole_Id(
            Long userId,
            Long roleId
    );
}
