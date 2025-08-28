package com.minlab.hospital.domain.repository;

import com.minlab.hospital.domain.entity.Patient;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long>, PatientRepositoryCustom {

    // 특정 병원의 특정 환자 조회
    Optional<Patient> findByHospital_IdAndIdAndStatus(Long hospitalId, Long patientId, char status);

    // 특정 병원의 모든 환자 조회
    List<Patient> findByHospital_IdAndStatus(Long hospitalId, char status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT COALESCE(MAX(p.seq), 0) FROM Patient p WHERE p.hospital.id = :hospitalId")
    Long findMaxSeqByHospitalForUpdate(@Param("hospitalId") Long hospitalId);

}
