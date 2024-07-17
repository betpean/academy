package com.academy.service;

import com.academy.dto.BoardDTO;
import com.academy.dto.NoticeDTO;
import com.academy.dto.PageRequestDTO;
import com.academy.dto.PageResponseDTO;
import com.academy.entity.Notice;

import java.util.List;

public interface NoticeService {

    public Long upload(NoticeDTO noticeDTO, String email); //글등록

    public List<Notice> select();

    public PageResponseDTO<NoticeDTO> list(PageRequestDTO pageRequestDTO);   //리스트,페이징처리,목록

    public NoticeDTO view(Long nno); //글 보기

    public void modify(NoticeDTO noticeDTO);  //글 수정

    public void delete(Long nno);   //굴 삭제
}
