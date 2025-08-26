package com.minlab.hospital.presentation.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PatientSearchCondition {
    private String name;
    private String patientNumber;
    private String birthDate;
}
