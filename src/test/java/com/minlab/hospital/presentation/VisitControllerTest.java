package com.minlab.hospital.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minlab.hospital.application.service.VisitService;
import com.minlab.hospital.domain.entity.Hospital;
import com.minlab.hospital.domain.entity.Patient;
import com.minlab.hospital.domain.entity.Visit;
import com.minlab.hospital.presentation.controller.VisitController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VisitController.class)
class VisitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VisitService visitService;

    @Autowired
    private ObjectMapper objectMapper;

    private Visit registerSampleVisit(Long id) {
        Hospital hospital = Hospital.builder()
                .id(1L)
                .name("서울병원")
                .doctorName("김닥터")
                .providerNumber("1100001234")
                .build();

        Patient patient = Patient.builder()
                .id(1L)
                .name("홍길동")
                .patientNumber("P123")
                .gender("M")
                .build();

        return Visit.builder()
                .id(id)
                .hospital(hospital)
                .patient(patient)
                .visitDate(LocalDateTime.now())
                .visitStatus("진행중")
                .visitType("외래")
                .build();
    }

    @Test
    @DisplayName("방문 등록 성공")
    void registerVisit_success() throws Exception {
        Visit visit = registerSampleVisit(1L);

        Mockito.when(visitService.registerVisit(anyLong(), anyLong(), any(Visit.class)))
                .thenReturn(visit);

        mockMvc.perform(post("/api/visits/hospital/1/patient/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(visit)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.visitStatus", is("진행중")));
    }

    @Test
    @DisplayName("방문 등록 실패 (병원 없음)")
    void registerVisit_fail_hospitalNotFound() throws Exception {
        Visit visit = registerSampleVisit(null);

        Mockito.when(visitService.registerVisit(anyLong(), anyLong(), any(Visit.class)))
                .thenThrow(new IllegalArgumentException("해당 병원을 찾을 수 없습니다."));

        mockMvc.perform(post("/api/visits/hospital/999/patient/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(visit)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("해당 병원을 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("방문 수정 성공")
    void updateVisit_success() throws Exception {
        Visit updateInfo = registerSampleVisit(1L);
        updateInfo.setVisitStatus("종료");

        Mockito.when(visitService.updateVisit(anyLong(), any(Visit.class)))
                .thenReturn(updateInfo);

        mockMvc.perform(put("/api/visits/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateInfo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.visitStatus", is("종료")));
    }

    @Test
    @DisplayName("방문 수정 실패 (존재하지 않음)")
    void updateVisit_fail_notFound() throws Exception {
        Visit updateInfo = registerSampleVisit(null);

        Mockito.when(visitService.updateVisit(anyLong(), any(Visit.class)))
                .thenThrow(new IllegalArgumentException("해당 방문을 찾을 수 없습니다."));

        mockMvc.perform(put("/api/visits/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateInfo)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("해당 방문을 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("방문 삭제 성공")
    void deleteVisit_success() throws Exception {
        mockMvc.perform(delete("/api/visits/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("방문 삭제 실패 (존재하지 않음)")
    void deleteVisit_fail_notFound() throws Exception {
        Mockito.doThrow(new IllegalArgumentException("해당 방문을 찾을 수 없습니다."))
                .when(visitService).deleteVisit(anyLong());

        mockMvc.perform(delete("/api/visits/999"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("해당 방문을 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("방문 단건 조회 성공")
    void getVisitById_success() throws Exception {
        Visit visit = registerSampleVisit(1L);

        Mockito.when(visitService.getVisit(1L)).thenReturn(visit);

        mockMvc.perform(get("/api/visits/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.visitStatus", is("진행중")));
    }

    @Test
    @DisplayName("방문 단건 조회 실패 (존재하지 않음)")
    void getVisitById_fail_notFound() throws Exception {
        Mockito.when(visitService.getVisit(anyLong()))
                .thenThrow(new IllegalArgumentException("해당 방문을 찾을 수 없습니다."));

        mockMvc.perform(get("/api/visits/999"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("해당 방문을 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("방문 전체 조회 성공")
    void getAllVisits_success() throws Exception {
        List<Visit> visits = Arrays.asList(
                registerSampleVisit(1L),
                registerSampleVisit(2L)
        );

        Mockito.when(visitService.getAllVisits()).thenReturn(visits);

        mockMvc.perform(get("/api/visits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));
    }
}
