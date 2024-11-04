package com.academy.service;

import com.academy.dto.AdmDTO;
import com.academy.dto.BoardDTO;
import com.academy.dto.PageRequestDTO;
import com.academy.dto.PageResponseDTO;
import com.academy.entity.Adm;
import com.academy.entity.Board;

import java.util.List;

public interface AdmService {
    public Long upload(AdmDTO admDTO,String ano);

    public List<Board> select();

    public PageResponseDTO<AdmDTO> list(PageRequestDTO pageRequestDTO);

    public AdmDTO view(Long ano); //글 보기

    public void modify(AdmDTO admDTO);  //글 수정

    public void delete(Long ano);   //굴 삭제
}
