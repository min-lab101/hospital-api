package com.minlab.hospital.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minlab.hospital.application.service.VisitService;
import com.minlab.hospital.presentation.controller.VisitController;
import com.minlab.hospital.presentation.dto.request.VisitRequestDto;
import com.minlab.hospital.presentation.dto.response.VisitResponseDto;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VisitController.class)
@AutoConfigureRestDocs
class VisitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VisitService visitService;

    @Autowired
    private ObjectMapper objectMapper;

    private VisitRequestDto visitRequestDto() {
        VisitRequestDto dto = new VisitRequestDto();
        dto.setVisitDate(LocalDateTime.of(2025, 8, 27, 14, 30));
        dto.setVisitStatus("방문중");
        dto.setVisitType("외래");
        dto.setVisitCategory("안과");
        return dto;
    }

    private VisitResponseDto visitResponseDto(Long id, Long patientId) {
        return new VisitResponseDto(
                id,
                1L,
                patientId,
                "001-00001",
                LocalDateTime.of(2025, 8, 27, 14, 30),
                "방문중",
                "외래"
        );
    }

    @Test
    @DisplayName("방문 등록 성공")
    void registerVisit_success() throws Exception {
        var req = visitRequestDto();
        var res = visitResponseDto(1L, 1L);

        Mockito.when(visitService.registerVisit(anyLong(), any(VisitRequestDto.class)))
                .thenReturn(res);

        mockMvc.perform(RestDocumentationRequestBuilders
                        .post("/api/patients/{patientId}/visits", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andDo(document("visit-register-success",
                        pathParameters(
                                parameterWithName("patientId").description("환자 ID")
                        ),
                        requestFields(
                                fieldWithPath("visitDate").description("방문 일시"),
                                fieldWithPath("visitStatus").description("방문 상태"),
                                fieldWithPath("visitType").description("방문 유형"),
                                fieldWithPath("visitCategory").description("진료 과목 코드")
                        ),
                        responseFields(
                                fieldWithPath("id").description("방문 ID"),
                                fieldWithPath("hospitalId").description("병원 ID"),
                                fieldWithPath("patientId").description("환자 ID"),
                                fieldWithPath("patientNumber").description("환자 번호"),
                                fieldWithPath("visitDate").description("방문 일시"),
                                fieldWithPath("visitStatus").description("방문 상태"),
                                fieldWithPath("visitType").description("방문 유형")
                        )
                ));
    }

    @Test
    @DisplayName("방문 등록 실패 - 환자 없음")
    void registerVisit_fail() throws Exception {
        var req = visitRequestDto();

        Mockito.when(visitService.registerVisit(anyLong(), any(VisitRequestDto.class)))
                .thenThrow(new EntityNotFoundException("해당 환자를 찾을 수 없습니다."));

        mockMvc.perform(RestDocumentationRequestBuilders
                        .post("/api/patients/{patientId}/visits", 999L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound())
                .andDo(document("visit-register-fail",
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("error").description("에러 유형"),
                                fieldWithPath("message").description("에러 메시지"),
                                fieldWithPath("timestamp").description("에러 발생 시각")
                        )
                ));
    }


    @Test
    @DisplayName("방문 수정 성공")
    void updateVisit_success() throws Exception {
        var req = visitRequestDto();
        var res = visitResponseDto(1L, 1L);

        Mockito.when(visitService.updateVisit(anyLong(), any(VisitRequestDto.class)))
                .thenReturn(res);

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/patients/{patientId}/visits/{visitId}", 1L, 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andDo(document("visit-update-success",
                        pathParameters(
                                parameterWithName("patientId").description("환자 ID"),
                                parameterWithName("visitId").description("방문 ID")
                        ),
                        requestFields(
                                fieldWithPath("visitDate").description("방문 일시"),
                                fieldWithPath("visitStatus").description("방문 상태"),
                                fieldWithPath("visitType").description("방문 유형"),
                                fieldWithPath("visitCategory").description("진료 과목 코드")
                        ),
                        responseFields(
                                fieldWithPath("id").description("방문 ID"),
                                fieldWithPath("hospitalId").description("병원 ID"),
                                fieldWithPath("patientId").description("환자 ID"),
                                fieldWithPath("patientNumber").description("환자 번호"),
                                fieldWithPath("visitDate").description("방문 일시"),
                                fieldWithPath("visitStatus").description("방문 상태"),
                                fieldWithPath("visitType").description("방문 유형")
                        )
                ));
    }

    @Test
    @DisplayName("방문 수정 실패 - 없는 방문")
    void updateVisit_fail() throws Exception {
        var req = visitRequestDto();

        Mockito.when(visitService.updateVisit(anyLong(), any(VisitRequestDto.class)))
                .thenThrow(new EntityNotFoundException("해당 방문 기록을 찾을 수 없습니다."));

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/patients/{patientId}/visits/{visitId}", 1L, 999L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound())
                .andDo(document("visit-update-fail",
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("error").description("에러 유형"),
                                fieldWithPath("message").description("에러 메시지"),
                                fieldWithPath("timestamp").description("에러 발생 시각")
                        )
                ));
    }

    @Test
    @DisplayName("방문 삭제 성공")
    void deleteVisit_success() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/patients/{patientId}/visits/{visitId}", 1L, 1L))
                .andExpect(status().isNoContent())
                .andDo(document("visit-delete-success",
                        pathParameters(
                                parameterWithName("patientId").description("환자 ID"),
                                parameterWithName("visitId").description("방문 ID")
                        )
                ));
    }

    @Test
    @DisplayName("방문 삭제 실패 - 없는 방문")
    void deleteVisit_fail() throws Exception {
        Mockito.doThrow(new EntityNotFoundException("해당 방문 기록을 찾을 수 없습니다."))
                .when(visitService).deleteVisit(999L);

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/patients/{patientId}/visits/{visitId}", 1L, 999L))
                .andExpect(status().isNotFound())
                .andDo(document("visit-delete-fail",
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("error").description("에러 유형"),
                                fieldWithPath("message").description("에러 메시지"),
                                fieldWithPath("timestamp").description("에러 발생 시각")
                        )
                ));
    }

    @Test
    @DisplayName("방문 단건 조회 성공")
    void getVisit_success() throws Exception {
        var res = visitResponseDto(1L, 1L);

        Mockito.when(visitService.getVisit(1L)).thenReturn(res);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/patients/{patientId}/visits/{visitId}", 1L, 1L))
                .andExpect(status().isOk())
                .andDo(document("visit-get-success",
                        pathParameters(
                                parameterWithName("patientId").description("환자 ID"),
                                parameterWithName("visitId").description("방문 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("방문 ID"),
                                fieldWithPath("hospitalId").description("병원 ID"),
                                fieldWithPath("patientId").description("환자 ID"),
                                fieldWithPath("patientNumber").description("환자 번호"),
                                fieldWithPath("visitDate").description("방문 일시"),
                                fieldWithPath("visitStatus").description("방문 상태"),
                                fieldWithPath("visitType").description("방문 유형")
                        )
                ));
    }

    @Test
    @DisplayName("방문 조회 실패 - 없는 방문")
    void getVisit_fail() throws Exception {
        Mockito.when(visitService.getVisit(999L))
                .thenThrow(new EntityNotFoundException("해당 방문 기록을 찾을 수 없습니다."));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/patients/{patientId}/visits/{visitId}", 1L, 999L))
                .andExpect(status().isNotFound())
                .andDo(document("visit-get-fail",
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("error").description("에러 유형"),
                                fieldWithPath("message").description("에러 메시지"),
                                fieldWithPath("timestamp").description("에러 발생 시각")
                        )
                ));
    }

    @Test
    @DisplayName("환자별 방문 전체 조회 성공")
    void getAllVisitsByPatient_success() throws Exception {
        var res = List.of(
                visitResponseDto(1L, 1L),
                visitResponseDto(2L, 1L)
        );

        Mockito.when(visitService.getVisitsByPatient(anyLong(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(res, PageRequest.of(0, 10), res.size()));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/patients/{patientId}/visits", 1L)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andDo(document("visit-get-all-success",
                        pathParameters(
                                parameterWithName("patientId").description("환자 ID")
                        ),
                        queryParameters(
                                parameterWithName("page").description("페이지 번호 (0부터 시작)"),
                                parameterWithName("size").description("페이지 크기")
                        ),
                        responseFields(
                                fieldWithPath("content[].id").description("방문 ID"),
                                fieldWithPath("content[].hospitalId").description("병원 ID"),
                                fieldWithPath("content[].patientId").description("환자 ID"),
                                fieldWithPath("content[].patientNumber").description("환자 번호"),
                                fieldWithPath("content[].visitDate").description("방문 일시"),
                                fieldWithPath("content[].visitStatus").description("방문 상태"),
                                fieldWithPath("content[].visitType").description("방문 유형"),

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
    @DisplayName("환자별 방문 조회 실패 - 환자 없음")
    void getVisitsByPatient_fail() throws Exception {
        Mockito.when(visitService.getVisitsByPatient(Mockito.eq(999L), Mockito.any(Pageable.class)))
                .thenThrow(new EntityNotFoundException("해당 환자를 찾을 수 없습니다."));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/patients/{patientId}/visits", 999L)
                        .param("pageNo", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isNotFound())
                .andDo(document("visit-get-fail",
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("error").description("에러 유형"),
                                fieldWithPath("message").description("에러 메시지"),
                                fieldWithPath("timestamp").description("에러 발생 시각")
                        )
                ));
    }

}
