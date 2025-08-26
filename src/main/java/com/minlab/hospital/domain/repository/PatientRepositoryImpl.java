package com.minlab.hospital.domain.repository;


import com.minlab.hospital.domain.entity.Patient;
import com.minlab.hospital.domain.entity.QPatient;
import com.minlab.hospital.presentation.dto.request.PatientSearchCondition;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PatientRepositoryImpl implements PatientRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Patient> searchPatients(Long hospitalId, PatientSearchCondition condition) {
        QPatient patient = QPatient.patient;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(patient.hospital.id.eq(hospitalId));

        if (condition.getName() != null && !condition.getName().isBlank()) {
            builder.and(patient.name.containsIgnoreCase(condition.getName()));
        }
        if (condition.getPatientNumber() != null && !condition.getPatientNumber().isBlank()) {
            builder.and(patient.patientNumber.eq(condition.getPatientNumber()));
        }
        if (condition.getBirthDate() != null && !condition.getBirthDate().isBlank()) {
            builder.and(patient.birthDate.stringValue().eq(condition.getBirthDate()));
        }

        return queryFactory
                .selectFrom(patient)
                .where(builder)
                .fetch();
    }
}
