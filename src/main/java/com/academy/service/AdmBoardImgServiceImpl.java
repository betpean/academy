package com.academy.service;

import com.academy.dto.BoardImgDTO;
import com.academy.entity.AdmBoard;
import com.academy.entity.Board;
import com.academy.entity.BoardImg;
import com.academy.repository.BoardImgRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class AdmBoardImgServiceImpl implements AdmBoardImgService {

    private final BoardImgRepository boardImgRepository;

    @Value("C:/academy/admboard")
    private String uploadpath1;

    public String makeDir1() {
        Date date = new Date();
        SimpleDateFormat yymmdd = new SimpleDateFormat("yyMMdd");
        String now = yymmdd.format(date);
        //240513

        String path1 = uploadpath1 + "\\" + now; //풀 경로
        //c:/upload/240513
        //application에서 설정한 path + \\ + 오늘 날짜 = 경로

        File file = new File(path1);

        if (file.exists() == false) {       //exists 파일이 존재한다면 true, x false
            file.mkdir(); //폴더 생성
        }

        return path1;

    }

    @Override
    public void register(Long abno, List<MultipartFile> multipartFileList) {
        log.info("boardimgserviceBno : " + abno);

        if (multipartFileList != null && multipartFileList.size() > 0) {

            for (MultipartFile a : multipartFileList) {
                if (a.getOriginalFilename().length() == 0) {
                    continue;
                }
                String origin = a.getOriginalFilename();
                log.info(origin);
                //    /aaa/aaa/aa.png
                String orignName1 = origin.substring(origin.lastIndexOf("\\") + 1);
                //파일명이 생김 file 객체의 경로는 경로+파일명

                String filepath = makeDir1(); //파일을 저장할 경로
                // c:/upload/240513/개발자.png
                String uuid1 = UUID.randomUUID().toString();
                log.info("유유아이디 : " + uuid1);

                //db저장용
                String newName1 = uuid1 + "_" + orignName1;

                //저장시 사용
                String saveName = filepath + "\\" + uuid1 + "_" + orignName1;
                log.info(saveName);
                File save = new File(saveName);

                try {

                    a.transferTo(save);

                } catch (IOException e) {

                }
                //저장한 파일의 제반사항을 db에 저장
                AdmBoard admBoard = AdmBoard.builder().abno(abno).build();

                BoardImg boardImg = new BoardImg();
                boardImg.setOriImgName(orignName1);
                boardImg.setImgName(newName1);
                boardImg.setAdmboard(admBoard);

                boardImgRepository.save(boardImg);
                log.info("모...목좀...풀겠습니다...");


            }

        }


    }

    @Override
    public List<BoardImgDTO> admimglist(Long abno) {
        List<BoardImg> admboardImgList
                = boardImgRepository.findByadmboard_AbnoOrderByIno(abno);

        List<BoardImgDTO> boardImgDTOList1
                = new ArrayList<>();

        for (BoardImg a : admboardImgList) {
            BoardImgDTO boardImgDTO = new BoardImgDTO();
            boardImgDTO = boardImgDTO.of(a);
            boardImgDTOList1.add(boardImgDTO);
            log.info("날짜 : " + boardImgDTO.getRegDate());

        }
        log.info("으아아아아아!!!!");

        return boardImgDTOList1;
    }


    @Override
    public void modify(Long[] ino) {
        for (int i = 0; i < ino.length; i++) {
            if (ino[i] != null && ino[i] > 0) { //ino가 null이 아니고 빈칸이 아닐 때
                BoardImg boardImg = boardImgRepository.findById(ino[i]).get();

                BoardImgDTO boardImgDTO = BoardImgDTO.of(boardImg);

                LocalDate as = boardImgDTO.getRegDate();
                String aa = as.format(DateTimeFormatter.ofPattern("yyMMdd"));

                String path = uploadpath1 + aa + boardImgDTO.getImgName();

                log.info(path);


                boardImgRepository.deleteById(ino[i]);


            }
        }
    }
}
