package com.donar.api.user.repository;

import com.donar.api.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = {"bloodType", "rhFactor"})
    Optional<User> findWithBloodTypeAndRhFactorById(Long id);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
