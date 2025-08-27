package com.minlab.hospital.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class VisitRequestDto {

    @NotNull(message = "접수 일시는 필수입니다.")
    private LocalDateTime visitDate;

    @NotBlank(message = "방문 상태는 필수입니다.")
    private String visitStatus;

    @NotBlank(message = "진료 유형은 필수입니다.")
    private String visitType;

    @NotBlank(message = "진료 과목은 필수입니다.")
    private String visitCategory;
}
