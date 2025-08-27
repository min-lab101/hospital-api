package com.minlab.hospital.presentation.controller;

import com.minlab.hospital.application.service.VisitService;
import com.minlab.hospital.presentation.dto.request.VisitRequestDto;
import com.minlab.hospital.presentation.dto.response.VisitResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients/{patientId}/visits")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;

    /**
     * 방문 등록
     */
    @PostMapping
    public ResponseEntity<VisitResponseDto> registerVisit(
            @PathVariable Long patientId,
            @Valid @RequestBody VisitRequestDto requestDto
    ) {
        return ResponseEntity.ok(visitService.registerVisit(patientId, requestDto));
    }

    /**
     * 방문 수정
     */
    @PutMapping("/{visitId}")
    public ResponseEntity<VisitResponseDto> updateVisit(
            @PathVariable Long patientId,
            @PathVariable Long visitId,
            @Valid @RequestBody VisitRequestDto requestDto
    ) {
        return ResponseEntity.ok(visitService.updateVisit(visitId, requestDto));
    }

    /**
     * 방문 삭제
     */
    @DeleteMapping("/{visitId}")
    public ResponseEntity<Void> deleteVisit(
            @PathVariable Long patientId,
            @PathVariable Long visitId
    ) {
        visitService.deleteVisit(visitId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 방문 단건 조회
     */
    @GetMapping("/{visitId}")
    public ResponseEntity<VisitResponseDto> getVisit(
            @PathVariable Long patientId,
            @PathVariable Long visitId
    ) {
        return ResponseEntity.ok(visitService.getVisit(visitId));
    }

    /**
     * 환자별 방문 전체 조회
     */
    @GetMapping
    public ResponseEntity<List<VisitResponseDto>> getAllVisitsByPatient(
            @PathVariable Long patientId
    ) {
        return ResponseEntity.ok(visitService.getVisitsByPatient(patientId));
    }
}
