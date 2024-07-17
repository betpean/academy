package com.academy.dto;

import com.academy.entity.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AdmBoardDTO {

    private Long abno; // 학원글번호

    private String title;   //제목

    private String content; //내용

    private User adu;    //작성자

    private LocalDate regDate;  //등록일자

    private LocalDateTime updateTime;  //수정일자

}
