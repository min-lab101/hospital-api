package com.minlab.hospital.application.service;

import com.minlab.hospital.domain.entity.Patient;
import com.minlab.hospital.domain.entity.Visit;
import com.minlab.hospital.domain.repository.PatientRepository;
import com.minlab.hospital.domain.repository.VisitRepository;
import com.minlab.hospital.presentation.dto.request.VisitRequestDto;
import com.minlab.hospital.presentation.dto.response.VisitResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VisitService {

    private final VisitRepository visitRepository;
    private final PatientRepository patientRepository;

    /**
     * 방문 등록
     */
    @Transactional
    public VisitResponseDto registerVisit(Long patientId, VisitRequestDto requestDto) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("해당 환자를 찾을 수 없습니다."));

        Visit visit = Visit.builder()
                .patient(patient)
                .hospital(patient.getHospital()) // 환자에서 병원 가져오기
                .visitDate(requestDto.getVisitDate())
                .visitStatus(requestDto.getVisitStatus())
                .visitType(requestDto.getVisitType())
                .visitCategory(requestDto.getVisitCategory())
                .build();

        return VisitResponseDto.fromEntity(visitRepository.save(visit));
    }

    /**
     * 방문 수정
     */
    @Transactional
    public VisitResponseDto updateVisit(Long visitId, VisitRequestDto requestDto) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new EntityNotFoundException("해당 방문을 찾을 수 없습니다."));

        visit.setVisitDate(requestDto.getVisitDate());
        visit.setVisitStatus(requestDto.getVisitStatus());
        visit.setVisitType(requestDto.getVisitType());
        visit.setVisitCategory(requestDto.getVisitCategory());

        return VisitResponseDto.fromEntity(visit);
    }

    /**
     * 방문 삭제
     */
    @Transactional
    public void deleteVisit(Long visitId) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new EntityNotFoundException("해당 방문을 찾을 수 없습니다."));

        visitRepository.delete(visit);
    }

    /**
     * 방문 단건 조회
     */
    public VisitResponseDto getVisit(Long visitId) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new EntityNotFoundException("해당 방문을 찾을 수 없습니다."));
        return VisitResponseDto.fromEntity(visit);
    }

    /**
     * 특정 환자 방문 전체 조회
     */
    public Page<VisitResponseDto> getVisitsByPatient(Long patientId, Pageable pageable) {
        if (!patientRepository.existsById(patientId)) {
            throw new EntityNotFoundException("해당 환자를 찾을 수 없습니다.");
        }

        Page<Visit> visitPage = visitRepository.findByPatient_Id(patientId, pageable);

        return visitPage.map(VisitResponseDto::fromEntity);
    }
}
