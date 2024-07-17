package com.academy.service;

import com.academy.entity.BoardImg;
import com.academy.repository.BoardImgRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class ImgService {

    @Value("${itemImgLocation}")
    private String itemImgLocation;
    //DB 저장을 위해서
    private final BoardImgRepository boardImgRepository;
    // 물리적인 저장을 위해서
    private final FileService fileService;


    //이미 itemService에서 dto -> entity로 변환이 된 상태
    //이미 itemService에서 MultipartFile 리스트로 입력받아서 for문으로 돌리는 중
    public void register(BoardImg boardImg, MultipartFile multipartFile) throws Exception{
        // 화면에서 넘겨받은 이미지파일 multipartFile 에서 파일명을 반환하는 메소드
        String oriImgName = multipartFile.getOriginalFilename();
        // 선언만 아래에서 변환
        String imgName = "";
        String imgUrl = "";

        //파일업로드
        // 입력받은 multipartFile 파일명이 없다면
        // 화면단에서 파일업로드 폼만있고 이미지를 넣지 않았다면 name="itemImgFile" 의
        // getOriginalFilename() 실행시 결과값이 ""  << null 이 아니다.
//        if(oriImgName != null && oriImgName != "" && oriImgName.length() != 0){
//            // 아래와 같은조건  //oriImgName 이 비어있지 않을때
//        }
        if(!StringUtils.isEmpty(oriImgName)){
            imgName = fileService.uploadFile(itemImgLocation, oriImgName,
                    multipartFile.getBytes());
            //확장자를 제외하고  uuid를 붙인 경로
            imgUrl = "/images/board/" + imgName;

            // 상품 이미지 정보 저장 DB
            //파라미터로 입력받은 itemImg entity 에 가공된 데이터를 set!!!!!

        }


        boardImg.updateBoardImg(oriImgName, imgName, imgUrl);
        boardImgRepository.save(boardImg);


    }

    // 이미지 업데이트

    public void updateBoardImg (Long itemImgId, MultipartFile multipartFile) throws Exception{
        //이미지 파일 비어있는지 확인 // 이유는 우리는 화면에서 input file을 다 열어놔서
        //빈 값이 올수 있음
        if (!multipartFile.isEmpty()) {
            //이미지 가져오기 아이디를 가지고
            BoardImg savedBoardImg = boardImgRepository
                    .findById(itemImgId).orElseThrow(EntityNotFoundException::new);
            //기존 물리적 이미지파일을 삭제
            if (  !StringUtils.isEmpty(savedBoardImg.getImgName())) {
                fileService.deleteFile(itemImgLocation +  "/"  + savedBoardImg.getImgName() );
            }
            // 입력받은 이미지파일의 명가져와서 fileService에 있는 물리적인 파일 저장
            String orImgName = multipartFile.getOriginalFilename();
            String imgName = fileService.
                    uploadFile(itemImgLocation, orImgName, multipartFile.getBytes());
            // /images/item
            String imgUrl = "/images/board/" + imgName;
            // 엔티티는 현재 영속 상태이므로 데이터를 변경하는 것만으로도
            // 변경감지기능 동작하여 트랜잭션이 끝날때 update 쿼리 실행
            savedBoardImg.updateBoardImg(orImgName, imgName, imgUrl);


        }





    }




}
