package com.academy.dto;

import com.academy.entity.User;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    private String replyText;    //내용
    private String replyer;





}

