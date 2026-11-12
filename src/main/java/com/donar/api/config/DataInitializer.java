
package com.donar.api.config;

import com.donar.api.bloodtype.entity.BloodType;
import com.donar.api.bloodtype.repository.IBloodTypeRepository;
import com.donar.api.rhfactor.entity.RhFactor;
import com.donar.api.rhfactor.repository.IRhFactorRepository;
import com.donar.api.role.entity.Role;
import com.donar.api.role.repository.IRoleRepository;
import com.donar.api.user.entity.User;
import com.donar.api.user.enums.Gender;
import com.donar.api.user.repository.IUserRepository;
import com.donar.api.userrole.entity.UserRole;
import com.donar.api.userrole.repository.IUserRoleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements ApplicationRunner {

    private final IBloodTypeRepository bloodTypeRepository;
    private final IRhFactorRepository rhFactorRepository;
    private final IRoleRepository roleRepository;
    private final IUserRepository userRepository;
    private final IUserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${ADMIN_FIRST_NAME}")
    private String adminFirstName;

    @Value("${ADMIN_LAST_NAME}")
    private String adminLastName;

    @Value("${ADMIN_BIRTH_DATE}")
    private LocalDate adminBirthDate;

    @Value("${ADMIN_EMAIL}")
    private String adminEmail;

    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;

    public DataInitializer(
            IBloodTypeRepository bloodTypeRepository,
            IRhFactorRepository rhFactorRepository,
            IRoleRepository roleRepository,
            IUserRepository userRepository,
            IUserRoleRepository userRoleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.bloodTypeRepository = bloodTypeRepository;
        this.rhFactorRepository = rhFactorRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        initializeBloodTypes();
        initializeRhFactors();
        initializeRoles();
        initializeAdmin();
    }

    private void initializeBloodTypes() {
        List.of("A", "B", "AB", "O").forEach(name -> {
            if (!bloodTypeRepository.existsByName(name)) {
                bloodTypeRepository.save(new BloodType(null, name));
            }
        });
    }

    private void initializeRhFactors() {
        List.of("Positivo", "Negativo").forEach(name -> {
            if (!rhFactorRepository.existsByName(name)) {
                rhFactorRepository.save(new RhFactor(null, name));
            }
        });
    }

    private void initializeRoles() {
        List.of("ADMIN", "HEMOADMIN", "USER").forEach(name -> {
            if (!roleRepository.existsByName(name)) {
                roleRepository.save(new Role(null, name));
            }
        });
    }

    private void initializeAdmin() {

        if (userRepository.existsByEmail(adminEmail)) {
            return;
        }

        BloodType bloodType = bloodTypeRepository.findByName("O")
                .orElseThrow(() -> new IllegalStateException("Blood type O not found"));

        RhFactor rhFactor = rhFactorRepository.findByName("Positivo")
                .orElseThrow(() -> new IllegalStateException("Rh factor Positivo not found"));

        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new IllegalStateException("ADMIN role not found"));

        LocalDateTime now = LocalDateTime.now();

        User admin = User.builder()
                .firstName(adminFirstName)
                .lastName(adminLastName)
                .birthDate(adminBirthDate)
                .email(adminEmail)
                .password(passwordEncoder.encode(adminPassword))
                .status(true)
                .gender(Gender.OTHER)
                .bloodType(bloodType)
                .rhFactor(rhFactor)
                .createdAt(now)
                .build();

        User savedAdmin = userRepository.save(admin);

        UserRole assignment = UserRole.builder()
                .user(savedAdmin)
                .role(adminRole)
                .status(true)
                .createdAt(now)
                .build();

        userRoleRepository.save(assignment);
    }
}
