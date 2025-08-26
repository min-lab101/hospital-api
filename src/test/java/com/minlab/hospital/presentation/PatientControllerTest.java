package com.minlab.hospital.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minlab.hospital.application.service.PatientService;
import com.minlab.hospital.presentation.controller.PatientController;
import com.minlab.hospital.presentation.dto.request.PatientRequestDto;
import com.minlab.hospital.presentation.dto.request.PatientSearchRequestDto;
import com.minlab.hospital.presentation.dto.response.PatientResponseDto;
import com.minlab.hospital.presentation.dto.response.PatientSearchResponseDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
                name,
                patientNumber,
                "M",
                LocalDate.of(1990, 1, 1),
                "010-1234-5678",
                "서울시 강남구"
        );
    }

    private PatientSearchResponseDto searchResponseDto(Long id, String patientNumber, String name){
        return new PatientSearchResponseDto(
                id,
                patientNumber,
                name,
                "M",
                LocalDate.of(1990, 1, 1),
                "010-1234-5678",
                "서울시 강남구",
                LocalDateTime.of(2024, 1, 1, 15, 44, 22)
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
                                fieldWithPath("address").description("주소")
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
                                fieldWithPath("address").description("주소")
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
                                fieldWithPath("address").description("주소")
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
                                fieldWithPath("[].address").description("주소")
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
        var res = List.of(searchResponseDto(1L, "1", "홍길동"));
        
        Mockito.when(patientService.searchPatients(anyLong(), any(PatientSearchRequestDto.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(res, PageRequest.of(0, 10), res.size()));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/hospitals/{hospitalId}/patients/search", 1L)
                        .param("name", "홍길동")
                        .param("patientNumber", "1")
                        .param("birthDate", "1990-01-01")
                        .param("pageNo", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andDo(document("patient-search-success",
                        pathParameters(
                                parameterWithName("hospitalId").description("병원 ID")
                        ),
                        queryParameters(
                                parameterWithName("name").description("검색할 환자 이름"),
                                parameterWithName("patientNumber").description("검색할 환자 번호"),
                                parameterWithName("birthDate").description("검색할 생년월일 (yyyy-MM-dd)"),
                                parameterWithName("pageNo").description("페이지 번호 (기본값 1)"),
                                parameterWithName("pageSize").description("페이지 크기 (기본값 10)")
                        ),
                        responseFields(
                                fieldWithPath("content[].id").description("환자 ID"),
                                fieldWithPath("content[].patientNumber").description("환자 번호"),
                                fieldWithPath("content[].name").description("이름"),
                                fieldWithPath("content[].gender").description("성별"),
                                fieldWithPath("content[].birthDate").description("생년월일"),
                                fieldWithPath("content[].phone").description("전화번호"),
                                fieldWithPath("content[].address").description("주소"),
                                fieldWithPath("content[].recentVisitDate").description("최근 방문 일자"),

                                // 페이지 정보
                                fieldWithPath("pageable.pageNumber").description("현재 페이지 번호 (0부터 시작)"),
                                fieldWithPath("pageable.pageSize").description("페이지 크기"),
                                fieldWithPath("pageable.offset").description("현재 페이지 시작 위치"),
                                fieldWithPath("pageable.paged").description("페이징 여부"),
                                fieldWithPath("pageable.unpaged").description("페이징되지 않았는지 여부"),
                                fieldWithPath("pageable.sort.empty").description("정렬 정보가 비어있는지 여부"),
                                fieldWithPath("pageable.sort.sorted").description("정렬 여부"),
                                fieldWithPath("pageable.sort.unsorted").description("정렬되지 않았는지 여부"),

                                fieldWithPath("last").description("마지막 페이지 여부"),
                                fieldWithPath("totalPages").description("전체 페이지 수"),
                                fieldWithPath("totalElements").description("전체 데이터 수"),
                                fieldWithPath("size").description("페이지 크기"),
                                fieldWithPath("number").description("현재 페이지 번호"),
                                fieldWithPath("first").description("첫 페이지 여부"),
                                fieldWithPath("numberOfElements").description("현재 페이지 요소 수"),
                                fieldWithPath("empty").description("결과가 비었는지 여부"),

                                // 최상위 sort
                                fieldWithPath("sort.empty").description("정렬 정보가 비어있는지 여부"),
                                fieldWithPath("sort.sorted").description("정렬 여부"),
                                fieldWithPath("sort.unsorted").description("정렬되지 않았는지 여부")
                        )

                ));
    }

    @Test
    @DisplayName("환자 검색 실패 - 병원 없음")
    void searchPatients_fail() throws Exception {
        Mockito.when(patientService.searchPatients(anyLong(), any(PatientSearchRequestDto.class), any(Pageable.class)))
                .thenThrow(new EntityNotFoundException("해당 병원을 찾을 수 없습니다."));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/hospitals/{hospitalId}/patients/search", 999L)
                        .param("name", "홍길동")
                        .param("pageNo", "1")
                        .param("pageSize", "10"))
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
