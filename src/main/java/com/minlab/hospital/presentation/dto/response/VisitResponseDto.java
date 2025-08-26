package com.minlab.hospital.presentation.dto.response;

import com.minlab.hospital.domain.entity.Visit;

import java.time.LocalDateTime;

public record VisitResponseDto(
        Long id,
        LocalDateTime visitDate,
        String visitStatus,
        String visitType
) {
    public static VisitResponseDto fromEntity(Visit visit) {
        return new VisitResponseDto(
                visit.getId(),
                visit.getVisitDate(),
                visit.getVisitStatus(),
                visit.getVisitType()
        );
    }
}
