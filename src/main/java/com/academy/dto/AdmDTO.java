package com.academy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AdmDTO {

    @NotBlank(message = "이메일을 입력해주세요")
    private String email;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min = 8, max = 16,
            message = "비밀번호는 8자 이상, " +
                    "16자 이하로 입력해주세요.")
    private String password;    //비번

    @NotBlank(message = "대표자명을 입력해주세요")
    private String rs;      //대표자명

    @NotBlank(message = "학원명을 입력해주세요")
    private String adm;     //학원명

    private String admkind; //학원종류

    @NotBlank(message = "주소를 입력해주세요")
    private String admaddress; //학원주소

    @NotBlank(message = "전화번호를 입력해주세요")
    private String admnumber;  //학원전화번호

    private Long ano;


}
