package com.minlab.hospital.domain.repository;

import com.minlab.hospital.domain.entity.Visit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface VisitRepository extends JpaRepository<Visit, Long> {

    // 특정 환자의 모든 방문 조회
    Page<Visit> findByPatient_Id(Long patientId, Pageable pageable);

}
