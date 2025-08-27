package com.minlab.hospital.application.service;

import com.minlab.hospital.domain.entity.Hospital;
import com.minlab.hospital.domain.repository.HospitalRepository;
import com.minlab.hospital.presentation.dto.request.HospitalRequestDto;
import com.minlab.hospital.presentation.dto.response.HospitalResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HospitalService {

    private final HospitalRepository hospitalRepository;

    /**
     * 병원 등록
     */
    @Transactional
    public HospitalResponseDto registerHospital(HospitalRequestDto requestDto) {
        Hospital hospital = Hospital.builder()
                .name(requestDto.getName())
                .providerNumber(requestDto.getProviderNumber())
                .doctorName(requestDto.getDoctorName())
                .build();

        Hospital saved = hospitalRepository.save(hospital);
        return HospitalResponseDto.fromEntity(saved);
    }

    /**
     * 병원 수정
     */
    @Transactional
    public HospitalResponseDto updateHospital(Long hospitalId, HospitalRequestDto requestDto) {
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new EntityNotFoundException("해당 병원이 존재하지 않습니다. id"));

        hospital.setName(requestDto.getName());
        hospital.setProviderNumber(requestDto.getProviderNumber());
        hospital.setDoctorName(requestDto.getDoctorName());

        return HospitalResponseDto.fromEntity(hospital);
    }

    /**
     * 병원 삭제
     */
    @Transactional
    public void deleteHospital(Long hospitalId) {
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new EntityNotFoundException("해당 병원이 존재하지 않습니다. id"));

        hospitalRepository.delete(hospital);
    }

    /**
     * 단건 조회
     */
    public HospitalResponseDto getHospital(Long hospitalId) {
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new EntityNotFoundException("해당 병원이 존재하지 않습니다. id"));

        return HospitalResponseDto.fromEntity(hospital);
    }

    /**
     * 전체 조회
     */
    public List<HospitalResponseDto> getAllHospitals() {
        return hospitalRepository.findAll()
                .stream()
                .map(HospitalResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

}
