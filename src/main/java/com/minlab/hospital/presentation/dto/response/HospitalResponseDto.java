package com.minlab.hospital.presentation.dto.response;

import com.minlab.hospital.domain.entity.Hospital;

public record HospitalResponseDto (
        Long id,
        String name,
        String providerNumber,
        String doctorName
){
    public static HospitalResponseDto fromEntity(Hospital hospital) {
        return new HospitalResponseDto(hospital.getId(), hospital.getName(), hospital.getProviderNumber(), hospital.getDoctorName());
    }
}
