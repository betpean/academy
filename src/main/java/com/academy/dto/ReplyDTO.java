package com.academy.dto;

import com.academy.entity.User;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString(exclude = {"board","notice","adm"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplyDTO {

    private Long rno;   //댓글 번호

    private Long bno;   //게시글
    private Long ano;   //학원
    private Long nno;   //공지사항

    private String content2;    //내용

    private String user;  //작성자

    private LocalDate regDate; //작성날짜

    private LocalDate modDate; //수정날짜




}
