package com.donar.api.user.service;

import com.donar.api.bloodtype.entity.BloodType;
import com.donar.api.bloodtype.repository.IBloodTypeRepository;
import com.donar.api.rhfactor.entity.RhFactor;
import com.donar.api.rhfactor.repository.IRhFactorRepository;
import com.donar.api.role.entity.Role;
import com.donar.api.role.repository.IRoleRepository;
import com.donar.api.user.dto.CreateUserRequest;
import com.donar.api.user.dto.UpdateUserRequest;
import com.donar.api.user.entity.User;
import com.donar.api.user.repository.IUserRepository;
import com.donar.api.userrole.entity.UserRole;
import com.donar.api.userrole.repository.IUserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final IUserRepository userRepository;
    private final IBloodTypeRepository bloodTypeRepository;
    private final IRhFactorRepository rhFactorRepository;
    private final IRoleRepository roleRepository;
    private final IUserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public User create(CreateUserRequest requestDto){
        if (existsByEmail(requestDto.email())) {
            throw new RuntimeException("Email already exists");
        }

        BloodType bloodType = bloodTypeRepository.findById(requestDto.bloodTypeId()).orElseThrow(() -> new RuntimeException("Blood type not found"));
        RhFactor rhFactor = rhFactorRepository.findById(requestDto.rhFactorId()).orElseThrow(() -> new RuntimeException("Rh factor not found"));
        Role userRole = roleRepository.findByName("USER").orElseThrow(() -> new RuntimeException("User role not found"));

        User user = User.builder()
                .firstName(requestDto.firstName())
                .lastName(requestDto.lastName())
                .birthDate(requestDto.birthDate())
                .email(requestDto.email())
                .password(passwordEncoder.encode(requestDto.password()))
                .gender(requestDto.gender())
                .bloodType(bloodType)
                .rhFactor(rhFactor)
                .status(true)
                .createdAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);

        UserRole userRoleAssignment = UserRole.builder()
                .user(savedUser)
                .role(userRole)
                .validFrom(LocalDate.now())
                .status(true)
                .createdAt(LocalDateTime.now())
                .build();

        userRoleRepository.save(userRoleAssignment);
        return savedUser;
    }

    @Transactional
    public User update(Long id, UpdateUserRequest requestDto) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        BloodType bloodType = bloodTypeRepository.findById(requestDto.bloodTypeId())
                .orElseThrow(() -> new RuntimeException("Blood type not found"));

        RhFactor rhFactor = rhFactorRepository.findById(requestDto.rhFactorId())
                .orElseThrow(() -> new RuntimeException("Rh factor not found"));

        user.setFirstName(requestDto.firstName());
        user.setLastName(requestDto.lastName());
        user.setBirthDate(requestDto.birthDate());
        user.setGender(requestDto.gender());
        user.setBloodType(bloodType);
        user.setRhFactor(rhFactor);
        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }
}
