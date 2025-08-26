package com.minlab.hospital.presentation.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PatientSearchRequestDto {
    private String name;
    private String patientNumber;
    private String birthDate;

    private Integer pageNo = 1;
    private Integer pageSize = 10;
}
