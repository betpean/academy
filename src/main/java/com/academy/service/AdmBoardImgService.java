package com.academy.service;

import com.academy.dto.BoardImgDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AdmBoardImgService {
    public void register(Long abno, List<MultipartFile> multipartFileList);

    public List<BoardImgDTO> admimglist(Long abno);

    public void modify(Long[] ino);

}
