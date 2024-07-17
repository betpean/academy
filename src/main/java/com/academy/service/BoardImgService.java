package com.academy.service;

import com.academy.dto.BoardImgDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface BoardImgService {

  public void register(Long bno, List<MultipartFile> multipartFileList);

  public List<BoardImgDTO> imglist(Long bno);

  public void modify(Long[] ino);

}
