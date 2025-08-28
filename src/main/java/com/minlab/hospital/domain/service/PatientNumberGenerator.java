package com.minlab.hospital.domain.service;

import org.springframework.stereotype.Component;

@Component
public class PatientNumberGenerator {
    public String generate(Long hospitalId, Long nextSeq) {
        return String.format("%03d-%d", hospitalId, nextSeq);
    }
}
