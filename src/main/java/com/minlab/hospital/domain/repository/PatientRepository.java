package com.minlab.hospital.domain.repository;

import com.minlab.hospital.domain.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    // 병원별 환자등록번호 중복 체크
    boolean existsByHospital_IdAndPatientNumber(Long hospitalId, String patientNumber);

    // 병원 + 환자등록번호로 특정 환자 찾기
    Optional<Patient> findByHospital_IdAndPatientNumber(Long hospitalId, String patientNumber);
}
