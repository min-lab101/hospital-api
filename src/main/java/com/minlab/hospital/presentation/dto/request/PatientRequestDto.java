package com.minlab.hospital.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class  PatientRequestDto {

    @NotBlank(message = "환자 이름은 필수입니다.")
    private String name;

    @NotBlank(message = "성별은 필수입니다.")
    private String gender;

    @Past(message = "생년월일은 과거 날짜여야 합니다.")
    private LocalDate birthDate;

    @NotBlank(message = "전화번호는 필수입니다.")
    private String phone;

    private String address;

}
