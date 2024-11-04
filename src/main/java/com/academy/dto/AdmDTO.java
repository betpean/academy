package com.academy.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class AdmDTO {

    @NotBlank
    private int ano;      //학원글번호

    @NotBlank(message = "학원명을 입력해주세요")
    private String admname;     //학원명

    private String admkind; //학원종류

    @NotBlank(message = "주소를 입력해주세요")
    private String admaddress; //학원주소

    @NotBlank(message = "전화번호를 입력해주세요")
    private String admnumber;  //학원전화번호

}
