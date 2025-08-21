package com.minlab.hospital.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minlab.hospital.application.service.PatientService;
import com.minlab.hospital.domain.entity.Patient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PatientService patientService;

    @Autowired
    private ObjectMapper objectMapper;

    private Patient createSamplePatient(Long id) {
        return Patient.builder()
                .id(id)
                .name("홍길동")
                .patientNumber("P123")
                .gender("M")
                .birthDate(LocalDate.of(1990, 1, 1))
                .phone("010-1234-5678")
                .address("서울시 강남구")
                .build();
    }

    @Test
    @DisplayName("환자 등록 성공")
    void registerPatient_success() throws Exception {
        Patient patient = createSamplePatient(1L);

        Mockito.when(patientService.registerPatient(anyLong(), any(Patient.class)))
                .thenReturn(patient);

        mockMvc.perform(post("/api/patients/hospital/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("홍길동")));
    }

    @Test
    @DisplayName("환자 등록 실패 - 병원 없음")
    void registerPatient_fail_hospitalNotFound() throws Exception {
        Patient patient = createSamplePatient(null);

        Mockito.when(patientService.registerPatient(anyLong(), any(Patient.class)))
                .thenThrow(new IllegalArgumentException("해당 병원을 찾을 수 없습니다."));

        mockMvc.perform(post("/api/patients/hospital/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("해당 병원을 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("환자 수정 성공")
    void updatePatient_success() throws Exception {
        Patient updateInfo = createSamplePatient(1L);
        updateInfo.setName("김철수");

        Mockito.when(patientService.updatePatient(anyLong(), any(Patient.class)))
                .thenReturn(updateInfo);

        mockMvc.perform(put("/api/patients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateInfo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("김철수")));
    }

    @Test
    @DisplayName("환자 수정 실패 - 존재하지 않는 환자")
    void updatePatient_fail_notFound() throws Exception {
        Patient updateInfo = createSamplePatient(null);
        updateInfo.setName("김철수");

        Mockito.when(patientService.updatePatient(anyLong(), any(Patient.class)))
                .thenThrow(new IllegalArgumentException("해당 환자를 찾을 수 없습니다."));

        mockMvc.perform(put("/api/patients/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateInfo)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("해당 환자를 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("환자 삭제 성공")
    void deletePatient_success() throws Exception {
        mockMvc.perform(delete("/api/patients/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("환자 삭제 실패 - 존재하지 않는 환자")
    void deletePatient_fail_notFound() throws Exception {
        Mockito.doThrow(new IllegalArgumentException("해당 환자를 찾을 수 없습니다."))
                .when(patientService).deletePatient(anyLong());

        mockMvc.perform(delete("/api/patients/999"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("해당 환자를 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("환자 단건 조회 성공")
    void getPatient_success() throws Exception {
        Patient patient = createSamplePatient(1L);

        Mockito.when(patientService.getPatient(1L)).thenReturn(patient);

        mockMvc.perform(get("/api/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("홍길동")));
    }

    @Test
    @DisplayName("환자 단건 조회 실패 - 존재하지 않음")
    void getPatient_fail_notFound() throws Exception {
        Mockito.when(patientService.getPatient(anyLong()))
                .thenThrow(new IllegalArgumentException("해당 환자를 찾을 수 없습니다."));

        mockMvc.perform(get("/api/patients/999"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("해당 환자를 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("환자 전체 조회 성공")
    void getAllPatients_success() throws Exception {
        List<Patient> patients = Arrays.asList(
                createSamplePatient(1L),
                createSamplePatient(2L)
        );

        Mockito.when(patientService.getAllPatients()).thenReturn(patients);

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));
    }
}
