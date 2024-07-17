package com.academy.service;

import com.academy.dto.*;

import java.security.Principal;
import java.util.List;

public interface AdmService {

    public Long upload(AdmBoardDTO admboardDTO, String email);

    public PageResponseDTO<AdmBoardDTO> list(PageRequestDTO pageRequestDTO);   //리스트,페이징처리,목록

    public AdmBoardDTO view(Long bno, PageRequestDTO pageRequestDTO, Principal principal); //글 보기

    public void modify(AdmBoardDTO admBoardDTO);  //글 수정

    public void delete(Long abno);   //굴 삭제

}

