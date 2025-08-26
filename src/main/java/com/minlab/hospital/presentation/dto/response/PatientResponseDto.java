package com.minlab.hospital.presentation.dto.response;

import com.minlab.hospital.domain.entity.Patient;

import java.time.LocalDate;
import java.util.List;

public record PatientResponseDto(
        Long id,
        String patientNumber,
        String name,
        String gender,
        LocalDate birthDate,
        String phone,
        String address,
        List<VisitResponseDto> visits
) {
    public static PatientResponseDto fromEntity(Patient patient) {
        return new PatientResponseDto(
                patient.getId(),
                patient.getPatientNumber(),
                patient.getName(),
                patient.getGender(),
                patient.getBirthDate(),
                patient.getPhone(),
                patient.getAddress(),
                patient.getVisits().stream()
                        .map(VisitResponseDto::fromEntity)
                        .toList()
        );
    }
}
