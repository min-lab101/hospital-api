package com.minlab.hospital.presentation;

import com.minlab.hospital.application.service.PatientService;
import com.minlab.hospital.domain.entity.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    /**
     * 환자 등록
     */
    @PostMapping("/hospital/{hospitalId}")
    public ResponseEntity<Patient> registerPatient(
            @PathVariable Long hospitalId,
            @RequestBody Patient patient
    ) {
        return ResponseEntity.ok(patientService.registerPatient(hospitalId, patient));
    }

    /**
     * 환자 수정
     */
    @PutMapping("/{patientId}")
    public ResponseEntity<Patient> updatePatient(
            @PathVariable Long patientId,
            @RequestBody Patient updateInfo
    ) {
        return ResponseEntity.ok(patientService.updatePatient(patientId, updateInfo));
    }


    /**
     * 환자 삭제
     */
    @DeleteMapping("/{patientId}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long patientId) {
        patientService.deletePatient(patientId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 환자 조회 (단건)
     */
    @GetMapping("/{patientId}")
    public ResponseEntity<Patient> getPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(patientService.getPatient(patientId));
    }

    /**
     * 환자 전체 조회
     */
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }
}
