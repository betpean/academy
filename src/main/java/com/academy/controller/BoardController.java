package com.academy.controller;

import com.academy.dto.BoardDTO;
import com.academy.dto.PageRequestDTO;
import com.academy.dto.PageResponseDTO;
import com.academy.dto.UserFormDTO;
import com.academy.entity.Board;
import com.academy.entity.User;
import com.academy.repository.UserRepository;
import com.academy.service.BoardService;
import com.academy.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {


    private final BoardService boardService;
    private final UserService userService;


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/upload")
    public void upload(Principal principal,Model model){
        log.info(principal.getName());
    }

    @PostMapping("/upload")
    public String uploadPost(@Valid BoardDTO boardDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               Principal principal) {

        //기존에 실습한 multipartFile을 받는 내용을 service에 만들어주고 넘겨준다.

        if (bindingResult.hasErrors()) {
            log.info("has errors.........");
            redirectAttributes
                    .addFlashAttribute("errors", bindingResult.getAllErrors());

            log.info(bindingResult.getAllErrors());
            return "redirect:/board/list";
        }



        Long bno = boardService.upload(boardDTO,principal.getName());

        redirectAttributes.addFlashAttribute("result", bno + "글이 등록되었습니다.");

        return "redirect:/board/list";
    }

    @GetMapping("/delete")
    public String boarddelete(){
        return "/board/delete";
    }

    @GetMapping("/modify")
    public String boardmodify(){
        return "/board/modify";
    }

    @GetMapping("/list")
    public String list(PageRequestDTO pageRequestDTO, Model model) {
        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);
        log.info(responseDTO);
        model.addAttribute("pageResponseDTO", responseDTO);
        return "/board/list"; // "board/list.html"로 보여줄 페이지를 지정
    }


    @GetMapping(value = "/view")
    public String boardview(Model model,PageRequestDTO pageRequestDTO,Long bno){

        BoardDTO boardDTO = boardService.view(bno);
        model.addAttribute("dto",boardDTO);
        log.info(bno);

        PageResponseDTO pageResponseDTO = boardService.list(pageRequestDTO);
//        model.addAttribute("pageResponseDTO",pageResponseDTO);

        return "board/view";
    }



}
