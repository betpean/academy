package com.academy.dto;


import com.academy.entity.User;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class UserFormDTO {


    //로그인 작업`
    //아이디, 비번 , 주소, 이름
    // 아이디는요 6~20글자
    //비밀번호는 8~16이내이여야하며


    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min = 8, max = 16,
            message = "비밀번호는 8자 이상, " +
                    "16자 이하로 입력해주세요.")
    private String password;    //비번

    @NotBlank(message = "이름을  입력해주세요.")
    private String name;    //이름

    @NotNull(message = "나이를 입력해주세요.")
    @Pattern(regexp = "^[0-9]*$", message = "나이는 숫자만 입력해주세요")
    private String age;

    private String gender;

    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email
    private String email;   //이메일


    private String address;     //주소





}
