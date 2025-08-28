package com.minlab.hospital.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patient",
        uniqueConstraints = { @UniqueConstraint(columnNames = {"hospital_id", "seq"}) })
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    @Column(nullable = false, length = 20)
    private String name; // 환자 이름

    @Column(nullable = false, length = 20, name = "patient_number")
    private String patientNumber; // 환자등록번호 (병원별 unique)

    @Column(nullable = false, length = 10)
    private String gender; // 성별

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate; // 생년월일

    @Column(length = 15)
    private String phone; // 휴대전화번호

    @Column(length = 100)
    private String address; // 주소

    @Column(nullable = false)
    private Long seq; // 병원별 순번, 무제한 증가

    @OneToMany(mappedBy = "patient")
    private List<Visit> visits = new ArrayList<>();

    @Column(nullable = false, length = 1)
    private char status = 'A'; // 'A': active, 'D': deleted

    public void softDelete() {
        this.status = 'D';
    }

    public boolean isActive() {
        return this.status == 'A';
    }

}
