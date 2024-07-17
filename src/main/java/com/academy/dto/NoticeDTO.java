package com.academy.dto;

import com.academy.entity.User;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NoticeDTO {

    private Long nno; //공지사항번호

    private String title; //제목

    private String content; //내용

    private User user;    //작성자

    private LocalDate regDate;

    private LocalDateTime updateTime;

}
