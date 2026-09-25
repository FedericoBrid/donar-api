package com.donar.api.security.service;

import com.donar.api.user.entity.User;
import com.donar.api.user.repository.IUserRepository;
import com.donar.api.userrole.entity.UserRole;
import com.donar.api.userrole.repository.IUserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final IUserRepository userRepository;
    private final IUserRoleRepository userRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuario no encontrado")
                );

        List<UserRole> userRoles =
                userRoleRepository.findByUser_IdAndStatusTrue(user.getId());

        List<SimpleGrantedAuthority> authorities = userRoles.stream()
                .map(userRole ->
                        new SimpleGrantedAuthority(
                                "ROLE_" + userRole.getRole().getName().toUpperCase()
                        )
                )
                .toList();

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .disabled(!user.getStatus())
                .build();
    }
}