package com.minlab.hospital.domain.repository;

import com.minlab.hospital.domain.entity.Patient;
import com.minlab.hospital.presentation.dto.request.PatientSearchCondition;

import java.util.List;

public interface PatientRepositoryCustom {
    List<Patient> searchPatients(Long hospitalId, PatientSearchCondition condition);
}