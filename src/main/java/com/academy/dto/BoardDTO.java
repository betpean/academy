package com.academy.dto;

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
public class BoardDTO {

    private Long bno;    //게시물번호

    private String title;   //제목

    private String content; //내용

    private String userId;    //작성자

    private LocalDate regDate;  //등록일자

    private LocalDateTime updateTime;  //수정일자



}
