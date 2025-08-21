package com.minlab.hospital.domain.repository;

import com.minlab.hospital.domain.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, Long> {

    // 특정 환자의 모든 방문 조회
    List<Visit> findByPatient_Id(Long patientId);

    // 특정 병원의 모든 방문 조회
    List<Visit> findByHospital_Id(Long hospitalId);
}
