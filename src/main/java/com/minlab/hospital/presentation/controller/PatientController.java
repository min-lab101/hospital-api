package com.minlab.hospital.presentation.controller;

import com.minlab.hospital.application.service.PatientService;
import com.minlab.hospital.presentation.dto.request.PatientRequestDto;
import com.minlab.hospital.presentation.dto.request.PatientSearchCondition;
import com.minlab.hospital.presentation.dto.response.PatientResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hospitals/{hospitalId}/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    /**
     * 환자 등록
     */
    @PostMapping
    public ResponseEntity<PatientResponseDto> registerPatient(
            @PathVariable Long hospitalId,
            @RequestBody PatientRequestDto requestDto
            ) {
        return ResponseEntity.ok(patientService.registerPatient(hospitalId, requestDto));
    }

    /**
     * 환자 수정
     */
    @PutMapping("/{patientId}")
    public ResponseEntity<PatientResponseDto> updatePatient(
            @PathVariable Long hospitalId,
            @PathVariable Long patientId,
            @RequestBody PatientRequestDto requestDto
    ) {
        return ResponseEntity.ok(patientService.updatePatient(hospitalId, patientId, requestDto));
    }

    /**
     * 환자 삭제
     */
    @DeleteMapping("/{patientId}")
    public ResponseEntity<Void> deletePatient(
            @PathVariable Long hospitalId,
            @PathVariable Long patientId
    ) {
        patientService.deletePatient(hospitalId, patientId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 환자 단건 조회
     */
    @GetMapping("/{patientId}")
    public ResponseEntity<PatientResponseDto> getPatient(
            @PathVariable Long hospitalId,
            @PathVariable Long patientId
    ) {
        return ResponseEntity.ok(patientService.getPatient(hospitalId, patientId));
    }

    /**
     * 병원별 환자 전체 조회
     */
    @GetMapping
    public ResponseEntity<List<PatientResponseDto>> getAllPatientsByHospital(
            @PathVariable Long hospitalId
    ) {
        return ResponseEntity.ok(patientService.getAllPatients(hospitalId));
    }

    /**
     * 환자 목록 조회 (조건)
     */
    @GetMapping("/search")
    public List<PatientResponseDto> searchPatients(
            @PathVariable Long hospitalId,
            PatientSearchCondition condition) {
        return patientService.searchPatients(hospitalId, condition);
    }
}
