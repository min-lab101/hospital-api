package com.minlab.hospital.presentation;

import com.minlab.hospital.domain.entity.Hospital;
import com.minlab.hospital.domain.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hospital")
public class HospitalController {

    private final HospitalRepository hospitalRepository;

    @GetMapping
    public ResponseEntity<List<Hospital>> findAll() {
        return ResponseEntity.ok(hospitalRepository.findAll());
    }
}