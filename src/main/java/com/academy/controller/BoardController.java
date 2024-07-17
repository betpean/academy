package com.academy.controller;

import com.academy.dto.*;
import com.academy.service.BoardImgService;
import com.academy.service.ImgService;
import com.academy.service.BoardService;
import com.academy.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.security.Principal;
import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {


    private final BoardService boardService;
    private final BoardImgService boardImgService;
    private final UserService userService;
    private final ImgService imgService;



    @PreAuthorize("isAuthenticated()")
    @GetMapping("/upload")
    public void upload(Principal principal,Model model){
        log.info(principal.getName());
    }

    @PostMapping("/upload")
    public String uploadPost(@Valid BoardDTO boardDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                             @RequestParam("boardImgFile") List<MultipartFile> boardImgFileList,
                               Principal principal) {

        //기존에 실습한 multipartFile을 받는 내용을 service에 만들어주고 넘겨준다.


        if (bindingResult.hasErrors()) {
            log.info("has errors.........");
            redirectAttributes
                    .addFlashAttribute("errors", bindingResult.getAllErrors());

            log.info(bindingResult.getAllErrors());
            return "redirect:/board/upload";
        }



        Long bno = boardService.upload(boardDTO,principal.getName());
        boardImgService.register(bno, boardImgFileList);

        redirectAttributes.addFlashAttribute("result", bno + "글이 등록되었습니다.");

        return "redirect:/board/list";
    }




    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model) {
        PageResponseDTO<BoardListReplyCountDTO> responseDTO = boardService.listWithReplyCount(pageRequestDTO);
      log.info(responseDTO);
        model.addAttribute("pageResponseDTO", responseDTO);

    }


    @GetMapping(value = "/view")
    public String boardview(Model model, PageRequestDTO pageRequestDTO, Long bno,Principal principal) {
        BoardDTO boardDTO = boardService.view(bno, pageRequestDTO);
        model.addAttribute("dto", boardDTO);
        model.addAttribute("imgdto", boardImgService.imglist(bno));

        if(principal == null){
            return "redirect:/user/login";
        }
        log.info("글번호 : " + bno);
        if (boardDTO != null) {
            log.info("아이디는: " + boardDTO.getUserId());
        } else {
            log.info("삐");
        }

        return "board/view";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{bno}")
    public String boardmodify(@PathVariable Long bno , Model model,PageRequestDTO pageRequestDTO){
        BoardDTO boardDTO = boardService.view(bno,pageRequestDTO);
        model.addAttribute("dto",boardDTO);
        model.addAttribute("imgdto", boardImgService.imglist(bno));
        return "/board/modify";
    }
    @PostMapping("/modify")
    public String boardmodifypost(@Valid BoardDTO boardDTO,
                                  BindingResult bindingResult ,
                                  RedirectAttributes redirectAttributes,
                                  PageRequestDTO pageRequestDTO,Long[] ino,
                                  @RequestParam("boardImgFile") List<MultipartFile> boardImgFileList){
        log.info("야 임마!" + ino);
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("errors",bindingResult.getAllErrors());
            redirectAttributes
                    .addAttribute("bno", boardDTO.getBno());
            return "redirect:/board/modify?bno=" +boardDTO.getBno();
        }
        boardService.modify(boardDTO);
        if( ino != null && ino.length > 0){
            boardImgService.modify(ino);
        }

        boardImgService.register(boardDTO.getBno(), boardImgFileList);
        redirectAttributes.addAttribute("bno", boardDTO.getBno());
        redirectAttributes.addFlashAttribute("result", boardDTO.getBno() + "글이 수정되었습니다.");
        return "redirect:/board/view?bno="+boardDTO.getBno();
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete")
    public String boarddelete(Long bno, RedirectAttributes redirectAttributes){

        log.info("여긴가?");
        boardService.delete(bno);
        redirectAttributes.addFlashAttribute("result", bno + " 번 글이 삭제되었습니다.");

        return "redirect:/board/list";
    }
    @PostMapping("/file")
    public String filePost(BoardDTO boardDTO,@RequestParam("file") MultipartFile[] file) {

        if (file != null && file.length > 0){

            for (int i = 0; i < file.length; i++){
                log.info(file[i].getContentType());
                log.info(file[i].getOriginalFilename());

                String path = "c:\\aaa\\" + file[i].getOriginalFilename();


                File save = new File(path);

                try {

                    file[i].transferTo(save);

                } catch (Exception e){

                }

            }

        }

    /*
    log.info(boardDTO);
    log.info(file);

    String contentType = file.getContentType();
    Long size = file.getSize();
    String origin = file.getOriginalFilename();
    log.info("파일 타입 : " + contentType);
    log.info("파일 사이즈 : " + size);
    log.info("파일명 : " + origin);

    File save = new File("c:\\aaa.png");*/


  /*  try {
      file.transferTo(save);
    } catch (IOException e) {

    }*/

        return null;
    }


}
