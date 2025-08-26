package com.minlab.hospital.presentation.controller;

import com.minlab.hospital.application.service.VisitService;
import com.minlab.hospital.domain.entity.Visit;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visits")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;

    /**
     * 방문 등록
     */
    @PostMapping("/hospital/{hospitalId}/patient/{patientId}")
    public ResponseEntity<Visit> registerVisit(
            @PathVariable Long hospitalId,
            @PathVariable Long patientId,
            @RequestBody Visit visit
    ) {
        return ResponseEntity.ok(visitService.registerVisit(hospitalId, patientId, visit));
    }

    /**
     * 방문 수정
     */
    @PutMapping("/{visitId}")
    public ResponseEntity<Visit> updateVisit(
            @PathVariable Long visitId,
            @RequestBody Visit updateInfo
    ) {
        return ResponseEntity.ok(visitService.updateVisit(visitId, updateInfo));
    }

    /**
     * 방문 삭제
     */
    @DeleteMapping("/{visitId}")
    public ResponseEntity<Void> deleteVisit(@PathVariable Long visitId) {
        visitService.deleteVisit(visitId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 방문 조회 (단건)
     */
    @GetMapping("/{visitId}")
    public ResponseEntity<Visit> getVisit(@PathVariable Long visitId) {
        return ResponseEntity.ok(visitService.getVisit(visitId));
    }

    /**
     * 방문 전체 조회
     */
    @GetMapping
    public ResponseEntity<List<Visit>> getAllVisits() {
        return ResponseEntity.ok(visitService.getAllVisits());
    }

    /**
     * 특정 환자의 방문 기록 조회
     */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Visit>> getVisitsByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(visitService.getVisitsByPatient(patientId));
    }

    /**
     * 특정 병원의 방문 기록 조회
     */
    @GetMapping("/hospital/{hospitalId}")
    public ResponseEntity<List<Visit>> getVisitsByHospital(@PathVariable Long hospitalId) {
        return ResponseEntity.ok(visitService.getVisitsByHospital(hospitalId));
    }
}
