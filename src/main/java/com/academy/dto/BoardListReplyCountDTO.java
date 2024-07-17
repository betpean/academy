package com.academy.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BoardListReplyCountDTO {

  private Long bno;
  private String content;
  private String title;
  private String user;

  private LocalDate regDate;

  private LocalDateTime updateTime;

  private Long replyCount; //댓글 총수


}
