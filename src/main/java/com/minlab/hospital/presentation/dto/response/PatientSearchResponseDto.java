package com.minlab.hospital.presentation.dto.response;

import com.minlab.hospital.domain.entity.Patient;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PatientSearchResponseDto(
        Long id,
        String name,
        String patientNumber,
        String gender,
        LocalDate birthDate,
        String phone,
        String address,
        LocalDateTime recentVisitDate
) {
    public static PatientSearchResponseDto fromEntity(Patient patient, LocalDateTime recentVisitDate) {
        return new PatientSearchResponseDto(
                patient.getId(),
                patient.getName(),
                patient.getPatientNumber(),
                patient.getGender(),
                patient.getBirthDate(),
                patient.getPhone(),
                patient.getAddress(),
                recentVisitDate
        );
    }
}