package com.academy.service;


import com.academy.dto.PageRequestDTO;
import com.academy.dto.PageResponseDTO;
import com.academy.dto.ReplyDTO;

import java.security.Principal;

public interface ReplyService {


  public Long register(ReplyDTO replyDTO , Principal principal);

  PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO);

//  public List<Board> select();

//  //페이징처리, 검색처리, 목록

//  //페이징처리, 검색처리, 댓글수량, 목록

  public ReplyDTO read(Long rno);

  public void modify(ReplyDTO replyDTO);

  public void remove(Long rno);



}
