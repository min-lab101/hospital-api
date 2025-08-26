package com.minlab.hospital.domain.repository;

import com.minlab.hospital.presentation.dto.request.PatientSearchRequestDto;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PatientRepositoryCustom {
    Page<Tuple> searchPatients(Long hospitalId, PatientSearchRequestDto condition, Pageable pageable);
}