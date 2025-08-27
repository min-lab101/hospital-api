package com.minlab.hospital.presentation.dto.response;

import com.minlab.hospital.domain.entity.Visit;

import java.time.LocalDateTime;

public record VisitResponseDto(
        Long id,
        Long hospitalId,
        Long patientId,
        String patientNumber,
        LocalDateTime visitDate,
        String visitStatus,
        String visitType
) {
    public static VisitResponseDto fromEntity(Visit visit) {
        return new VisitResponseDto(
                visit.getId(),
                visit.getHospital().getId(),
                visit.getPatient().getId(),
                visit.getPatient().getPatientNumber(),
                visit.getVisitDate(),
                visit.getVisitStatus(),
                visit.getVisitType()
        );
    }
}
