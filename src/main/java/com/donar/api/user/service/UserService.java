package com.donar.api.user.service;

import com.donar.api.bloodtype.entity.BloodType;
import com.donar.api.bloodtype.repository.IBloodTypeRepository;
import com.donar.api.rhfactor.entity.RhFactor;
import com.donar.api.rhfactor.repository.IRhFactorRepository;
import com.donar.api.user.dto.CreateUserRequest;
import com.donar.api.user.entity.User;
import com.donar.api.user.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final IUserRepository userRepository;
    private final IBloodTypeRepository bloodTypeRepository;
    private final IRhFactorRepository rhFactorRepository;

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

    public User create(CreateUserRequest requestDto){
        if (existsByEmail(requestDto.email())) {
            throw new RuntimeException("Email already exists");
        }

        BloodType bloodType = bloodTypeRepository.findById(requestDto.bloodTypeId()).orElseThrow(() -> new RuntimeException("Blood type not found"));
        RhFactor rhFactor = rhFactorRepository.findById(requestDto.rhFactorId()).orElseThrow(() -> new RuntimeException("Rh factor not found"));

        User user = User.builder()
                .firstName(requestDto.firstName())
                .lastName(requestDto.lastName())
                .birthDate(requestDto.birthDate())
                .email(requestDto.email())
                //password for the moment, without spring security
                .password(requestDto.password())
                .gender(requestDto.gender())
                .bloodType(bloodType)
                .rhFactor(rhFactor)
                .status(true)
                .createdAt(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }
}
