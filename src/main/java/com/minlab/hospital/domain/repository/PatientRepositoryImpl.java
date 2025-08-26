package com.minlab.hospital.domain.repository;

import com.minlab.hospital.domain.entity.QPatient;
import com.minlab.hospital.domain.entity.QVisit;
import com.minlab.hospital.presentation.dto.request.PatientSearchRequestDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class PatientRepositoryImpl implements PatientRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Tuple> searchPatients(Long hospitalId, PatientSearchRequestDto condition, Pageable pageable) {
        QPatient patient = QPatient.patient;
        QVisit visit = QVisit.visit;

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

        // 환자 + 최근 방문일
        List<Tuple> tuples = queryFactory
                .select(patient, visit.visitDate.max())
                .from(patient)
                .leftJoin(visit).on(visit.patient.eq(patient))
                .where(builder)
                .groupBy(patient.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 개수
        Long total = queryFactory
                .select(patient.count())
                .from(patient)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(tuples, pageable, total != null ? total : 0L);
    }
}
