package com.minlab.hospital.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Table(name = "hospital")
public class Hospital {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 45)
    private String name;                 // 병원명

    @Column(nullable = false, length = 20)
    private String providerNumber;       // 요양기관번호

    @Column(nullable = false, length = 10)
    private String doctorName;         // 병원장명

}
