package com.minlab.hospital.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minlab.hospital.application.service.PatientService;
import com.minlab.hospital.presentation.controller.PatientController;
import com.minlab.hospital.presentation.dto.request.PatientRequestDto;
import com.minlab.hospital.presentation.dto.request.PatientSearchCondition;
import com.minlab.hospital.presentation.dto.response.PatientResponseDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
@AutoConfigureRestDocs
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PatientService patientService;

    @Autowired
    private ObjectMapper objectMapper;

    private PatientRequestDto requestDto() {
        return new PatientRequestDto(
                "홍길동",
                "M",
                LocalDate.of(1990, 1, 1),
                "010-1234-5678",
                "서울시 강남구"
        );
    }

    private PatientResponseDto responseDto(Long id, String patientNumber, String name) {
        return new PatientResponseDto(
                id,
                patientNumber,
                name,
                "M",
                LocalDate.of(1990, 1, 1),
                "010-1234-5678",
                "서울시 강남구",
                List.of()
        );
    }

    @Test
    @DisplayName("환자 등록 성공")
    void registerPatient_success() throws Exception {
        var req = requestDto();
        var res = responseDto(1L, "1", "홍길동");

        Mockito.when(patientService.registerPatient(anyLong(), any())).thenReturn(res);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/hospitals/{hospitalId}/patients", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andDo(document("patient-register-success",
                        pathParameters(
                                parameterWithName("hospitalId").description("병원 ID")
                        ),
                        requestFields(
                                fieldWithPath("name").description("환자 이름"),
                                fieldWithPath("gender").description("성별"),
                                fieldWithPath("birthDate").description("생년월일"),
                                fieldWithPath("phone").description("전화번호"),
                                fieldWithPath("address").description("주소")
                        ),
                        responseFields(
                                fieldWithPath("id").description("환자 ID"),
                                fieldWithPath("patientNumber").description("환자 번호"),
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("gender").description("성별"),
                                fieldWithPath("birthDate").description("생년월일"),
                                fieldWithPath("phone").description("전화번호"),
                                fieldWithPath("address").description("주소"),
                                fieldWithPath("visits").description("진료 기록 목록")
                        )
                ));
    }

    @Test
    @DisplayName("환자 등록 실패 - 병원 없음")
    void registerPatient_fail() throws Exception {
        var req = requestDto();

        Mockito.when(patientService.registerPatient(anyLong(), any()))
                .thenThrow(new EntityNotFoundException("해당 병원을 찾을 수 없습니다."));

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/hospitals/{hospitalId}/patients", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound())
                .andDo(document("patient-register-fail",
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("error").description("에러 유형"),
                                fieldWithPath("message").description("에러 메시지"),
                                fieldWithPath("timestamp").description("에러 발생 시각")
                        )
                ));
    }

    @Test
    @DisplayName("환자 수정 성공")
    void updatePatient_success() throws Exception {
        var req = requestDto();
        var res = responseDto(1L, "1", "홍길동");

        Mockito.when(patientService.updatePatient(anyLong(), anyLong(), any())).thenReturn(res);

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/hospitals/{hospitalId}/patients/{patientId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andDo(document("patient-update-success",
                        pathParameters(
                                parameterWithName("hospitalId").description("병원 ID"),
                                parameterWithName("patientId").description("환자 ID")
                        ),
                        requestFields(
                                fieldWithPath("name").description("환자 이름"),
                                fieldWithPath("gender").description("성별"),
                                fieldWithPath("birthDate").description("생년월일"),
                                fieldWithPath("phone").description("전화번호"),
                                fieldWithPath("address").description("주소")
                        ),
                        responseFields(
                                fieldWithPath("id").description("환자 ID"),
                                fieldWithPath("patientNumber").description("환자 번호"),
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("gender").description("성별"),
                                fieldWithPath("birthDate").description("생년월일"),
                                fieldWithPath("phone").description("전화번호"),
                                fieldWithPath("address").description("주소"),
                                fieldWithPath("visits").description("진료 기록 목록")
                        )
                ));
    }

    @Test
    @DisplayName("환자 수정 실패 - 환자 없음")
    void updatePatient_fail() throws Exception {
        var req = requestDto();

        Mockito.when(patientService.updatePatient(anyLong(), anyLong(), any()))
                .thenThrow(new EntityNotFoundException("해당 환자를 찾을 수 없습니다."));

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/hospitals/{hospitalId}/patients/{patientId}", 1L, 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound())
                .andDo(document("patient-update-fail",
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("error").description("에러 유형"),
                                fieldWithPath("message").description("에러 메시지"),
                                fieldWithPath("timestamp").description("에러 발생 시각")
                        )
                ));
    }

    @Test
    @DisplayName("환자 삭제 성공")
    void deletePatient_success() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/hospitals/{hospitalId}/patients/{patientId}", 1L, 1L))
                .andExpect(status().isNoContent())
                .andDo(document("patient-delete-success",
                        pathParameters(
                                parameterWithName("hospitalId").description("병원 ID"),
                                parameterWithName("patientId").description("환자 ID")
                        )
                ));
    }

    @Test
    @DisplayName("환자 삭제 실패 - 환자 없음")
    void deletePatient_fail() throws Exception {
        Mockito.doThrow(new EntityNotFoundException("해당 환자를 찾을 수 없습니다."))
                .when(patientService).deletePatient(anyLong(), anyLong());

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/hospitals/{hospitalId}/patients/{patientId}", 1L, 999L))
                .andExpect(status().isNotFound())
                .andDo(document("patient-delete-fail",
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("error").description("에러 유형"),
                                fieldWithPath("message").description("에러 메시지"),
                                fieldWithPath("timestamp").description("에러 발생 시각")
                        )
                ));
    }

    @Test
    @DisplayName("환자 단건 조회 성공")
    void getPatient_success() throws Exception {
        var res = responseDto(1L, "1", "홍길동");

        Mockito.when(patientService.getPatient(anyLong(), anyLong())).thenReturn(res);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/hospitals/{hospitalId}/patients/{patientId}", 1L, 1L))
                .andExpect(status().isOk())
                .andDo(document("patient-get-success",
                        pathParameters(
                                parameterWithName("hospitalId").description("병원 ID"),
                                parameterWithName("patientId").description("환자 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("환자 ID"),
                                fieldWithPath("patientNumber").description("환자 번호"),
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("gender").description("성별"),
                                fieldWithPath("birthDate").description("생년월일"),
                                fieldWithPath("phone").description("전화번호"),
                                fieldWithPath("address").description("주소"),
                                fieldWithPath("visits").description("진료 기록 목록")
                        )
                ));
    }

    @Test
    @DisplayName("환자 단건 조회 실패 - 환자 없음")
    void getPatient_fail() throws Exception {
        Mockito.when(patientService.getPatient(anyLong(), anyLong()))
                .thenThrow(new EntityNotFoundException("해당 환자를 찾을 수 없습니다."));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/hospitals/{hospitalId}/patients/{patientId}", 1L, 999L))
                .andExpect(status().isNotFound())
                .andDo(document("patient-get-fail",
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("error").description("에러 유형"),
                                fieldWithPath("message").description("에러 메시지"),
                                fieldWithPath("timestamp").description("에러 발생 시각")
                        )
                ));
    }

    @Test
    @DisplayName("병원 환자 전체 조회 성공")
    void getAllPatients_success() throws Exception {
        var res = List.of(responseDto(1L, "1", "홍길동"));

        Mockito.when(patientService.getAllPatients(anyLong())).thenReturn(res);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/hospitals/{hospitalId}/patients", 1L))
                .andExpect(status().isOk())
                .andDo(document("patient-getAll-success",
                        pathParameters(
                                parameterWithName("hospitalId").description("병원 ID")
                        ),
                        responseFields(
                                fieldWithPath("[].id").description("환자 ID"),
                                fieldWithPath("[].patientNumber").description("환자 번호"),
                                fieldWithPath("[].name").description("이름"),
                                fieldWithPath("[].gender").description("성별"),
                                fieldWithPath("[].birthDate").description("생년월일"),
                                fieldWithPath("[].phone").description("전화번호"),
                                fieldWithPath("[].address").description("주소"),
                                fieldWithPath("[].visits").description("진료 기록 목록")
                        )
                ));
    }

    @Test
    @DisplayName("병원 환자 전체 조회 실패 - 병원 없음")
    void getAllPatients_fail() throws Exception {
        Mockito.when(patientService.getAllPatients(anyLong()))
                .thenThrow(new EntityNotFoundException("해당 병원을 찾을 수 없습니다."));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/hospitals/{hospitalId}/patients", 999L))
                .andExpect(status().isNotFound())
                .andDo(document("patient-getAll-fail",
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("error").description("에러 유형"),
                                fieldWithPath("message").description("에러 메시지"),
                                fieldWithPath("timestamp").description("에러 발생 시각")
                        )
                ));
    }

    @Test
    @DisplayName("환자 검색 성공")
    void searchPatients_success() throws Exception {
        var res = List.of(responseDto(1L, "1", "홍길동"));

        Mockito.when(patientService.searchPatients(anyLong(), any(PatientSearchCondition.class))).thenReturn(res);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/hospitals/{hospitalId}/patients/search", 1L)
                        .param("name", "홍길동"))
                .andExpect(status().isOk())
                .andDo(document("patient-search-success",
                        pathParameters(
                                parameterWithName("hospitalId").description("병원 ID")
                        ),
                        responseFields(
                                fieldWithPath("[].id").description("환자 ID"),
                                fieldWithPath("[].patientNumber").description("환자 번호"),
                                fieldWithPath("[].name").description("이름"),
                                fieldWithPath("[].gender").description("성별"),
                                fieldWithPath("[].birthDate").description("생년월일"),
                                fieldWithPath("[].phone").description("전화번호"),
                                fieldWithPath("[].address").description("주소"),
                                fieldWithPath("[].visits").description("진료 기록 목록")
                        )
                ));
    }

    @Test
    @DisplayName("환자 검색 실패 - 병원 없음")
    void searchPatients_fail() throws Exception {
        Mockito.when(patientService.searchPatients(anyLong(), any(PatientSearchCondition.class)))
                .thenThrow(new EntityNotFoundException("해당 병원을 찾을 수 없습니다."));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/hospitals/{hospitalId}/patients/search", 999L)
                        .param("name", "홍길동"))
                .andExpect(status().isNotFound())
                .andDo(document("patient-search-fail",
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("error").description("에러 유형"),
                                fieldWithPath("message").description("에러 메시지"),
                                fieldWithPath("timestamp").description("에러 발생 시각")
                        )
                ));
    }
}
