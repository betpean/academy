package com.academy.dto;


import com.academy.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserFormDTO {


    //로그인 작업`
    //아이디, 비번 , 주소, 이름
    // 아이디는요 6~20글자
    //비밀번호는 8~16이내이여야하며

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    private String id;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min = 8, max = 16,
            message = "비밀번호는 8자 이상, " +
                    "16자 이하로 입력해주세요.")
    private String password;    //비번

    @NotBlank(message = "이름을  입력해주세요.")
    private String name;    //이름


    private String age;


    private String gender;

    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email
    private String email;   //이메일

    private String adm;     //학원명

    private String admkind; //학원종류

    @NotEmpty(message = "주소는 필수 입력 값입니다.")
    private String address;     //주소

    private Long uno;






}
