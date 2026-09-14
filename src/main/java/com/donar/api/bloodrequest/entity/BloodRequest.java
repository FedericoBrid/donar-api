package com.donar.api.bloodrequest.entity;

import com.donar.api.bloodcenter.entity.BloodCenter;
import com.donar.api.bloodrequest.enums.RequestStatus;
import com.donar.api.bloodrequest.enums.Urgency;
import com.donar.api.bloodtype.entity.BloodType;
import com.donar.api.rhfactor.entity.RhFactor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "blood_request")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BloodRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "blood_center_id", referencedColumnName = "id", nullable = false)
    private BloodCenter bloodCenter;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "blood_type_id", referencedColumnName = "id", nullable = false)
    private BloodType bloodType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rh_factor_id", referencedColumnName = "id", nullable = false)
    private RhFactor rhFactor;

    @Enumerated(EnumType.STRING)
    @Column(name = "urgency", nullable = false)
    private Urgency urgency;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RequestStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;
}
