package com.academy.controller;

import com.academy.dto.*;
import com.academy.service.BoardService;
import com.academy.service.NoticeService;
import com.academy.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/")
public class MainController {

    private final BoardService boardService;
    private final NoticeService noticeService;
    private final UserService userService;



    @GetMapping("/")
    public String list(PageRequestDTO pageRequestDTO ,Model model) {


        PageResponseDTO<BoardListReplyCountDTO> responseDTO = boardService.listWithReplyCount(pageRequestDTO);
        log.info(responseDTO);
        model.addAttribute("pageResponseDTO", responseDTO);

        PageResponseDTO<NoticeDTO> responseDTO1 = noticeService.list(pageRequestDTO);
        model.addAttribute("pageResponseDTO1", responseDTO1);

        return "main";
    }
    @GetMapping("/registerjoin")
    public String registerjoin(){

        return "registerjoin";
    }
}
