package com.academy.service;


import com.academy.repository.BoardImgRepository;
import com.academy.dto.BoardImgDTO;
import com.academy.entity.Board;
import com.academy.entity.BoardImg;
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
public class BoardImgServiceImpl implements BoardImgService {

  private final BoardImgRepository boardImgRepository;


//  @Value("c:/upload/")
  @Value("${itemImgLocation}")
  private String uploadpath;

  //날짜별로 폴더를 생성하는 메소드
  public String makeDir(){
    Date date = new Date();
    SimpleDateFormat yymmdd = new SimpleDateFormat("yyMMdd");
    String now = yymmdd.format(date);
    //240513

    String path = uploadpath + "\\" + now; //풀 경로
    //c:/upload/240513
    //application에서 설정한 path + \\ + 오늘 날짜 = 경로

    File file = new File(path);

    if (file.exists() == false){       //exists 파일이 존재한다면 true, x false
        file.mkdir(); //폴더 생성
    }

    return path;

  }


  @Override
  public void register(Long bno, List<MultipartFile> multipartFileList) {
    log.info("boardimgserviceBno : "+bno);

    if (multipartFileList != null && multipartFileList.size() > 0){

      for (MultipartFile a : multipartFileList) {
        if (a.getOriginalFilename().length() == 0){
          continue;
        }
        String origin = a.getOriginalFilename();
        log.info(origin);
        //    /aaa/aaa/aa.png
        String orignName = origin.substring(origin.lastIndexOf("\\")+1);
        //파일명이 생김 file 객체의 경로는 경로+파일명

        String filepath = makeDir(); //파일을 저장할 경로
        // c:/upload/240513/개발자.png
        String uuid = UUID.randomUUID().toString();
        log.info("유유아이디 : " + uuid);

        //db저장용
        String newName = uuid + "_" + orignName;

        //저장시 사용
        String saveName = filepath + "\\" + uuid + "_" + orignName;
        log.info(saveName);
        File save = new File(saveName);

        try {

          a.transferTo(save);

        }catch (IOException e){

        }
        //저장한 파일의 제반사항을 db에 저장
        Board board = Board.builder().bno(bno).build();

        BoardImg boardImg = new BoardImg();
        boardImg.setOriImgName(orignName);
        boardImg.setImgName(newName);
        boardImg.setBoard(board);

        boardImgRepository.save(boardImg);
        log.info("모...목좀...풀겠습니다...");


      }

    }


  }

  @Override
  public List<BoardImgDTO> imglist(Long bno) {

    List<BoardImg> boardImgList
            = boardImgRepository.findByBoard_BnoOrderByIno(bno);

    List<BoardImgDTO> boardImgDTOList
            = new ArrayList<>();

    for (BoardImg a : boardImgList){
      BoardImgDTO boardImgDTO = new BoardImgDTO();
      boardImgDTO = boardImgDTO.of(a);
      boardImgDTOList.add(boardImgDTO);
      log.info("날짜 : "+ boardImgDTO.getRegDate());

    }
    log.info("으아아아아아!!!!");

    return boardImgDTOList ;
  }


  @Override
  public void modify(Long[] ino) {

//    log.info(Arrays.toString(ino));

    //이미지(ino)가 1개만 오지 않고 여러개 올 수 있으니까 배열로 ino를 받기
    //반복



    for (int i = 0; i<ino.length; i++) {
      if(ino[i] !=null && ino[i] > 0){ //ino가 null이 아니고 빈칸이 아닐 때
        BoardImg  boardImg =  boardImgRepository.findById(ino[i]).get();

        BoardImgDTO boardImgDTO = BoardImgDTO.of(boardImg);

        log.info(boardImgDTO+"여기이ㅣ..ㅠ.");

        LocalDate as = boardImgDTO.getRegDate();
        String aa = as.format(DateTimeFormatter.ofPattern("yyMMdd"));

        String path = uploadpath + aa + boardImgDTO.getImgName();

        log.info(path );

        boardImgRepository.deleteById(ino[i]);


    }
  }

    //리스트에 담긴 이미지를 전부 불러오기
    //modify에서 삭제 버튼을 눌러서 입력받은 번호만 삭제 > board/modify의 저장 버튼을 눌러야 실행

  }

  //File dele = new File(경로);
  // dele.delete();




}
