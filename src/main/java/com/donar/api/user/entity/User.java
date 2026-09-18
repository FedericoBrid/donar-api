package com.donar.api.user.entity;

import com.donar.api.bloodtype.entity.BloodType;
import com.donar.api.rhfactor.entity.RhFactor;
import com.donar.api.user.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "status", nullable = false)
    private Boolean status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "blood_type_id", referencedColumnName = "id", nullable = false)
    private BloodType bloodType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rh_factor_id", referencedColumnName = "id", nullable = false)
    private RhFactor rhFactor;
}
