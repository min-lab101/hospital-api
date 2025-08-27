package com.minlab.hospital.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minlab.hospital.application.service.HospitalService;
import com.minlab.hospital.presentation.controller.HospitalController;
import com.minlab.hospital.presentation.dto.request.HospitalRequestDto;
import com.minlab.hospital.presentation.dto.response.HospitalResponseDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HospitalController.class)
@AutoConfigureRestDocs
class HospitalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HospitalService hospitalService;

    @Autowired
    private ObjectMapper objectMapper;

    private HospitalRequestDto requestDto() {
        return HospitalRequestDto.builder()
                .name("강남병원")
                .providerNumber("123456")
                .doctorName("김철수")
                .build();
    }

    private HospitalResponseDto responseDto(Long id, String name) {
        return new HospitalResponseDto(id, name, "1100001234", "김병원장");
    }

    @Test
    @DisplayName("병원 등록 성공")
    void registerHospital_success() throws Exception {
        var req = requestDto();
        var res = responseDto(1L, "강남병원");

        Mockito.when(hospitalService.registerHospital(any())).thenReturn(res);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/hospitals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andDo(document("hospital-register-success",
                        requestFields(
                                fieldWithPath("name").description("병원 이름"),
                                fieldWithPath("providerNumber").description("요양기관번호"),
                                fieldWithPath("doctorName").description("병원장명")
                        ),
                        responseFields(
                                fieldWithPath("id").description("병원 ID"),
                                fieldWithPath("name").description("병원 이름"),
                                fieldWithPath("providerNumber").description("요양기관번호"),
                                fieldWithPath("doctorName").description("병원장명")
                        )
                ));
    }

    @Test
    @DisplayName("병원 등록 실패 - 필드 부족")
    void registerHospital_fail() throws Exception {
        var req = new HospitalRequestDto(); // 필드 비어있음

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/hospitals", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andDo(document("hospital-register-fail",
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("error").description("에러 유형"),
                                fieldWithPath("message").description("에러 메시지"),
                                fieldWithPath("timestamp").description("에러 발생 시각")
                        )
                ));
    }

    @Test
    @DisplayName("병원 수정 성공")
    void updateHospital_success() throws Exception {
        Long hospitalId = 1L;
        var req = HospitalRequestDto.builder()
                .name("강남병원 리뉴얼")
                .providerNumber("123456")
                .doctorName("김철수")
                .build();
        var res = responseDto(hospitalId, "강남병원 리뉴얼");

        Mockito.when(hospitalService.updateHospital(eq(hospitalId), any(HospitalRequestDto.class)))
                .thenReturn(res);

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/hospitals/{hospitalId}", hospitalId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andDo(document("hospital-update-success",
                        pathParameters(
                                parameterWithName("hospitalId").description("병원 ID")
                        ),
                        requestFields(
                                fieldWithPath("name").description("병원 이름"),
                                fieldWithPath("providerNumber").description("요양기관번호"),
                                fieldWithPath("doctorName").description("병원장명")
                        ),
                        responseFields(
                                fieldWithPath("id").description("병원 ID"),
                                fieldWithPath("name").description("병원 이름"),
                                fieldWithPath("providerNumber").description("요양기관번호"),
                                fieldWithPath("doctorName").description("병원장명")
                        )
                ));
    }

    @Test
    @DisplayName("병원 수정 실패 - 병원 없음")
    void updateHospital_fail() throws Exception {
        var req = requestDto();

        Mockito.when(hospitalService.updateHospital(anyLong(), any(HospitalRequestDto.class)))
                .thenThrow(new EntityNotFoundException("해당 병원이 존재하지 않습니다."));

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/hospitals/{hospitalId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound())
                .andDo(document("hospital-update-fail",
                        pathParameters(
                                parameterWithName("hospitalId").description("병원 ID")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("error").description("에러 유형"),
                                fieldWithPath("message").description("에러 메시지"),
                                fieldWithPath("timestamp").description("에러 발생 시각")
                        )
                ));
    }

    @Test
    @DisplayName("병원 삭제 성공")
    void deleteHospital_success() throws Exception {
        Mockito.doNothing().when(hospitalService).deleteHospital(1L);

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/hospitals/{hospitalId}", 1L))
                .andExpect(status().isNoContent())
                .andDo(document("hospital-delete-success",
                        pathParameters(
                                parameterWithName("hospitalId").description("병원 ID")
                        )
                ));
    }

    @Test
    @DisplayName("병원 삭제 실패 - 병원 없음")
    void deleteHospital_fail() throws Exception {
        Mockito.doThrow(new EntityNotFoundException("해당 병원을 찾을 수 없습니다."))
                .when(hospitalService).deleteHospital(anyLong());

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/hospitals/{hospitalId}", 999L))
                .andExpect(status().isNotFound())
                .andDo(document("hospital-delete-fail",
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("error").description("에러 유형"),
                                fieldWithPath("message").description("에러 메시지"),
                                fieldWithPath("timestamp").description("에러 발생 시각")
                        )
                ));
    }

    @Test
    @DisplayName("병원 단건 조회 성공")
    void getHospital_success() throws Exception {
        var res = responseDto(1L, "강남병원");

        Mockito.when(hospitalService.getHospital(anyLong())).thenReturn(res);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/hospitals/{hospitalId}", 1L))
                .andExpect(status().isOk())
                .andDo(document("hospital-get-success",
                        pathParameters(
                                parameterWithName("hospitalId").description("병원 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("병원 ID"),
                                fieldWithPath("name").description("병원 이름"),
                                fieldWithPath("providerNumber").description("요양기관번호"),
                                fieldWithPath("doctorName").description("병원장명")
                        )
                ));
    }

    @Test
    @DisplayName("병원 단건 조회 실패 - 병원 없음")
    void getHospital_fail() throws Exception {
        Long hospitalId = 999L;

        Mockito.when(hospitalService.getHospital(hospitalId))
                .thenThrow(new EntityNotFoundException("해당 병원을 찾을 수 없습니다."));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/hospitals/{hospitalId}", hospitalId))
                .andExpect(status().isNotFound())
                .andDo(document("hospital-get-fail",
                        pathParameters(
                                parameterWithName("hospitalId").description("병원 ID")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("error").description("에러 유형"),
                                fieldWithPath("message").description("에러 메시지"),
                                fieldWithPath("timestamp").description("에러 발생 시각")
                        )
                ));
    }

    @Test
    @DisplayName("병원 전체 조회 성공")
    void getAllHospitals_success() throws Exception {
        var res1 = responseDto(1L, "강남병원");
        var res2 = responseDto(2L, "역삼병원");

        Mockito.when(hospitalService.getAllHospitals())
                .thenReturn(List.of(res1, res2));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/hospitals"))
                .andExpect(status().isOk())
                .andDo(document("hospital-get-all-success",
                        responseFields(
                                fieldWithPath("[].id").description("병원 ID"),
                                fieldWithPath("[].name").description("병원 이름"),
                                fieldWithPath("[].providerNumber").description("요양기관번호"),
                                fieldWithPath("[].doctorName").description("병원장명")
                        )
                ));
    }
}