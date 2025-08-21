package com.minlab.hospital.config;

import com.minlab.hospital.domain.entity.Hospital;
import com.minlab.hospital.domain.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final HospitalRepository hospitalRepository;

    @Override
    public void run(String... args) {
        if (hospitalRepository.count() == 0) {
            hospitalRepository.save(Hospital.builder()
                    .name("민랩종합병원")
                    .providerNumber("1100001234")
                    .doctorName("김병원장")
                    .build());
            hospitalRepository.save(Hospital.builder()
                    .name("민랩의원")
                    .providerNumber("2200005678")
                    .doctorName("박원장")
                    .build());
        }
    }
}
