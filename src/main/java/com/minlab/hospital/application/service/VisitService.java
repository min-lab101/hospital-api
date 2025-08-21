package com.minlab.hospital.application.service;

import com.minlab.hospital.domain.entity.Hospital;
import com.minlab.hospital.domain.entity.Patient;
import com.minlab.hospital.domain.entity.Visit;
import com.minlab.hospital.domain.repository.HospitalRepository;
import com.minlab.hospital.domain.repository.PatientRepository;
import com.minlab.hospital.domain.repository.VisitRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VisitService {

    private final VisitRepository visitRepository;
    private final PatientRepository patientRepository;
    private final HospitalRepository hospitalRepository;

    /**
     * 방문 등록
     */
    @Transactional
    public Visit registerVisit(Long hospitalId, Long patientId, Visit visit) {
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new EntityNotFoundException("해당 병원을 찾을 수 없습니다."));
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("해당 환자를 찾을 수 없습니다."));

        visit.setHospital(hospital);
        visit.setPatient(patient);

        return visitRepository.save(visit);
    }

    /**
     * 방문 수정
     */
    @Transactional
    public Visit updateVisit(Long visitId, Visit updateInfo) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new EntityNotFoundException("해당 방문 기록을 찾을 수 없습니다."));

        visit.setVisitDate(updateInfo.getVisitDate());
        visit.setVisitStatus(updateInfo.getVisitStatus());
        visit.setVisitType(updateInfo.getVisitType());

        return visit;
    }

    /**
     * 방문 삭제
     */
    @Transactional
    public void deleteVisit(Long visitId) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new EntityNotFoundException("해당 방문 기록을 찾을 수 없습니다."));
        visitRepository.delete(visit);
    }

    /**
     * 방문 조회 (단건)
     */
    public Visit getVisit(Long visitId) {
        return visitRepository.findById(visitId)
                .orElseThrow(() -> new EntityNotFoundException("해당 방문 기록을 찾을 수 없습니다."));
    }

    /**
     * 방문 전체 조회
     */
    public List<Visit> getAllVisits() {
        return visitRepository.findAll();
    }

    /**
     * 특정 환자의 방문 기록 조회
     */
    public List<Visit> getVisitsByPatient(Long patientId) {
        return visitRepository.findByPatient_Id(patientId);
    }

    /**
     * 특정 병원의 방문 기록 조회
     */
    public List<Visit> getVisitsByHospital(Long hospitalId) {
        return visitRepository.findByHospital_Id(hospitalId);
    }
}
