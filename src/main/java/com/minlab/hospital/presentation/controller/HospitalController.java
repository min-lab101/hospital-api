package com.minlab.hospital.presentation.controller;

import com.minlab.hospital.application.service.HospitalService;
import com.minlab.hospital.presentation.dto.request.HospitalRequestDto;
import com.minlab.hospital.presentation.dto.response.HospitalResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hospitals")
public class HospitalController {

    private final HospitalService hospitalService;

    /**
     * 병원 등록
     */
    @PostMapping
    public ResponseEntity<HospitalResponseDto> createHospital(@Valid @RequestBody HospitalRequestDto requestDto) {
        HospitalResponseDto response = hospitalService.registerHospital(requestDto);
        return ResponseEntity.ok(response);
    }

    /**
     * 병원 수정
     */
    @PutMapping("/{hospitalId}")
    public ResponseEntity<HospitalResponseDto> updateHospital(@PathVariable Long hospitalId,
                                                              @Valid @RequestBody HospitalRequestDto requestDto) {
        HospitalResponseDto response = hospitalService.updateHospital(hospitalId, requestDto);
        return ResponseEntity.ok(response);
    }

    /**
     * 병원 삭제
     */
    @DeleteMapping("/{hospitalId}")
    public ResponseEntity<Void> deleteHospital(@PathVariable Long hospitalId) {
        hospitalService.deleteHospital(hospitalId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 단건 조회
     */
    @GetMapping("/{hospitalId}")
    public ResponseEntity<HospitalResponseDto> getHospital(@PathVariable Long hospitalId) {
        HospitalResponseDto response = hospitalService.getHospital(hospitalId);
        return ResponseEntity.ok(response);
    }

    /**
     * 전체 조회
     */
    @GetMapping
    public ResponseEntity<List<HospitalResponseDto>> getAllHospitals() {
        List<HospitalResponseDto> hospitals = hospitalService.getAllHospitals();
        return ResponseEntity.ok(hospitals);
    }
}