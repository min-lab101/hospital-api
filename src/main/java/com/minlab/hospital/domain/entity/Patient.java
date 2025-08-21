package com.minlab.hospital.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;

@Entity
@Table(name = "patient")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    @Column(nullable = false, length = 20)
    private String name; // 환자 이름

    @Column(nullable = false, length = 20)
    private String patientNumber; // 환자등록번호 (병원별 unique)

    @Column(nullable = false, length = 10)
    private String gender; // 성별

    @Column(nullable = false)
    private LocalDate birthDate; // 생년월일

    @Column(length = 15)
    private String phone; // 휴대전화번호

    @Column(length = 100)
    private String address; // 주소
}
