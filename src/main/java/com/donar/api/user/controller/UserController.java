package com.donar.api.user.controller;

import com.donar.api.user.dto.CreateUserRequest;
import com.donar.api.user.dto.UserResponse;
import com.donar.api.user.entity.User;
import com.donar.api.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@Valid @RequestBody CreateUserRequest createUserRequest) {

        User user = userService.create(createUserRequest);

        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getBirthDate(),
                user.getEmail(),
                user.getGender(),
                user.getBloodType().getId(),
                user.getBloodType().getName(),
                user.getRhFactor().getId(),
                user.getRhFactor().getName(),
                user.getStatus(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
