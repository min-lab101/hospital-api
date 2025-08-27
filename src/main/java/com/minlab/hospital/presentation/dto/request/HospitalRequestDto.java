package com.minlab.hospital.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalRequestDto {
    @NotBlank(message = "병원 이름은 필수입니다.")
    private String name;

    @NotBlank(message = "요양기관번호는 필수입니다.")
    private String providerNumber;

    @NotBlank(message = "병원장명은 필수입니다.")
    private String doctorName;
}
