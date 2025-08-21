package com.minlab.hospital.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "visit")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 병원과의 연관관계
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    /**
     * 환자와의 연관관계
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    /**
     * 접수 일시
     */
    @Column(nullable = false)
    private LocalDateTime visitDate;

    /**
     * 접수 상태 코드 (예: 방문중, 종료, 취소)
     */
    @Column(length = 20, nullable = false)
    private String visitStatus;

    /**
     * 진료 유형 (외래, 입원, 응급)
     */
    @Column(length = 20, nullable = false)
    private String visitType;
}
