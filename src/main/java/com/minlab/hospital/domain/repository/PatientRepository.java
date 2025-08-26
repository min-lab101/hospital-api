package com.minlab.hospital.domain.repository;

import com.minlab.hospital.domain.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long>, PatientRepositoryCustom {

    // 특정 병원의 특정 환자 조회
    Optional<Patient> findByHospital_IdAndId(Long hospitalId, Long patientId);

    // 특정 병원의 모든 환자 조회
    List<Patient> findByHospital_Id(Long hospitalId);

    // 병원별 가장 큰 환자등록번호 조회
    @Query("SELECT MAX(p.patientNumber) FROM Patient p WHERE p.hospital.id = :hospitalId")
    String findMaxPatientNumberByHospital(@Param("hospitalId") Long hospitalId);

}
