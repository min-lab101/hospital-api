package com.minlab.hospital.application.service;

import com.minlab.hospital.domain.entity.Hospital;
import com.minlab.hospital.domain.entity.Patient;
import com.minlab.hospital.domain.repository.HospitalRepository;
import com.minlab.hospital.domain.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PatientService {

    private final PatientRepository patientRepository;
    private final HospitalRepository hospitalRepository;

    /**
     * 환자 등록
     */
    @Transactional
    public Patient registerPatient(Long hospitalId, Patient patient) {
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new EntityNotFoundException("해당 병원을 찾을 수 없습니다."));

        // 병원별 환자등록번호 중복 체크
        if (patientRepository.existsByHospital_IdAndPatientNumber(hospitalId, patient.getPatientNumber())) {
            throw new IllegalArgumentException("해당 병원에 이미 존재하는 환자등록번호입니다.");
        }

        patient.setHospital(hospital);
        return patientRepository.save(patient);
    }
    /**
     * 환자 수정
     */
    @Transactional
    public Patient updatePatient(Long patientId, Patient updateInfo) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("해당 환자를 찾을 수 없습니다."));

        patient.setName(updateInfo.getName());
        patient.setGender(updateInfo.getGender());
        patient.setBirthDate(updateInfo.getBirthDate());
        patient.setPhone(updateInfo.getPhone());
        patient.setAddress(updateInfo.getAddress());

        return patient;
    }

    /**
     * 환자 삭제
     */
    @Transactional
    public void deletePatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("해당 환자를 찾을 수 없습니다."));
        patientRepository.delete(patient);
    }

    /**
     * 환자 조회 (단건)
     */
    public Patient getPatient(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("해당 환자를 찾을 수 없습니다."));
    }

    /**
     * 환자 전체 조회
     */
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }
}
