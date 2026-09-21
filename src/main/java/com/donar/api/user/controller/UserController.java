package com.donar.api.user.controller;

import com.donar.api.user.dto.CreateUserRequest;
import com.donar.api.user.dto.UpdateUserRequest;
import com.donar.api.user.dto.UserResponse;
import com.donar.api.user.entity.User;
import com.donar.api.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@Valid @RequestBody CreateUserRequest createUserRequest) {
        User user = userService.create(createUserRequest);
        return toResponse(user);
    }

    @GetMapping
    public List<UserResponse> findAll() {
        return userService.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public UserResponse findById(@PathVariable Long id) {
        User user = userService.findById(id);
        return toResponse(user);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse update(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        User user = userService.update(id, updateUserRequest);
        return toResponse(user);
    }

    //maybe later we will implement a /status for activate and deactivate, but for now we will just implement deactivate
    @PatchMapping("/{id}/deactivate")
    public UserResponse deactivate(@PathVariable Long id) {
        User user = userService.deactivate(id);
        return toResponse(user);
    }

    private UserResponse toResponse(User user) {
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
