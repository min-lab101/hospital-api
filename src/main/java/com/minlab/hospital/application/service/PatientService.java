package com.minlab.hospital.application.service;

import com.minlab.hospital.domain.entity.Hospital;
import com.minlab.hospital.domain.entity.Patient;
import com.minlab.hospital.domain.repository.HospitalRepository;
import com.minlab.hospital.domain.repository.PatientRepository;
import com.minlab.hospital.presentation.dto.request.PatientRequestDto;
import com.minlab.hospital.presentation.dto.request.PatientSearchCondition;
import com.minlab.hospital.presentation.dto.response.PatientResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    public PatientResponseDto registerPatient(Long hospitalId, PatientRequestDto requestDto) {
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new EntityNotFoundException("해당 병원을 찾을 수 없습니다."));

        // 병원별 가장 큰 등록번호 조회 후 +1
        String maxNumber = patientRepository.findMaxPatientNumberByHospital(hospitalId);
        String nextNumber = (maxNumber == null) ? "1" : String.valueOf(Integer.parseInt(maxNumber) + 1);

        Patient patient = Patient.builder()
                .hospital(hospital)
                .patientNumber(nextNumber)
                .name(requestDto.getName())
                .gender(requestDto.getGender())
                .birthDate(requestDto.getBirthDate())
                .phone(requestDto.getPhone())
                .address(requestDto.getAddress())
                .build();

        return PatientResponseDto.fromEntity(patientRepository.save(patient));
    }

    /**
     * 환자 수정 (등록번호는 변경 불가)
     */
    @Transactional
    public PatientResponseDto updatePatient(Long hospitalId, Long patientId, PatientRequestDto requestDto) {
        Patient patient = patientRepository.findByHospital_IdAndId(hospitalId, patientId)
                .orElseThrow(() -> new EntityNotFoundException("해당 병원에서 환자를 찾을 수 없습니다."));

        // 환자등록번호는 변경 불가 (비즈니스 규칙)
        patient.setName(requestDto.getName());
        patient.setGender(requestDto.getGender());
        patient.setBirthDate(requestDto.getBirthDate());
        patient.setPhone(requestDto.getPhone());
        patient.setAddress(requestDto.getAddress());

        return PatientResponseDto.fromEntity(patient);
    }

    /**
     * 환자 삭제
     */
    @Transactional
    public void deletePatient(Long hospitalId, Long patientId) {
        Patient patient = patientRepository.findByHospital_IdAndId(hospitalId, patientId)
                .orElseThrow(() -> new EntityNotFoundException("해당 병원에서 환자를 찾을 수 없습니다."));

        patientRepository.delete(patient);
    }

    /**
     * 환자 단건 조회
     */
    public PatientResponseDto getPatient(Long hospitalId, Long patientId) {
        Patient patient = patientRepository.findByHospital_IdAndId(hospitalId, patientId)
                .orElseThrow(() -> new EntityNotFoundException("해당 병원에서 환자를 찾을 수 없습니다."));

        return PatientResponseDto.fromEntity(patient);
    }

    /**
     * 환자 전체 조회
     */
    public List<PatientResponseDto> getAllPatients(Long hospitalId) {
        // 병원 존재 여부 확인
        if (!hospitalRepository.existsById(hospitalId)) {
            throw new EntityNotFoundException("해당 병원을 찾을 수 없습니다.");
        }

        return patientRepository.findByHospital_Id(hospitalId)
                .stream()
                .map(PatientResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 환자 목록 조회 (조건)
     */
    public List<PatientResponseDto> searchPatients(Long hospitalId, PatientSearchCondition condition) {
        // 병원 존재 여부 확인
        if (!hospitalRepository.existsById(hospitalId)) {
            throw new EntityNotFoundException("해당 병원을 찾을 수 없습니다.");
        }

        return patientRepository.searchPatients(hospitalId, condition)
                .stream()
                .map(PatientResponseDto::fromEntity)
                .toList();
    }

}
