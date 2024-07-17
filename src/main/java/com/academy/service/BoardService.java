package com.academy.service;

import com.academy.dto.BoardDTO;
import com.academy.dto.BoardListReplyCountDTO;
import com.academy.dto.PageRequestDTO;
import com.academy.dto.PageResponseDTO;
import com.academy.entity.Board;

import java.util.List;

public interface BoardService {

    public Long upload(BoardDTO boardDTO,String email); //글등록

    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);   //리스트,페이징처리,목록

    //페이징처리, 검색처리, 댓글수량, 목록
    public PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);

    public BoardDTO view(Long bno,PageRequestDTO pageRequestDTO); //글 보기

    public void modify(BoardDTO boardDTO);  //글 수정

    public void delete(Long bno);   //굴 삭제
}
