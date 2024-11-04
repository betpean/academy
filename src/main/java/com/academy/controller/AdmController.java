package com.academy.controller;

import com.academy.dto.AdmDTO;
import com.academy.dto.PageRequestDTO;
import com.academy.service.AdmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/adm")
public class AdmController {

    @GetMapping("/upload")
    public String admupload(){

        return "/adm/upload";
    }

    @GetMapping("/modify")
    public String admmodify(){
        return "/adm/modify";
    }
//    @GetMapping("/list")
//    public String admlist(Principal principal , PageRequestDTO pageRequestDTO, Model model, AdmDTO admDTO){
//        if(principal == null){
//            return "redirect:/";
//        }
//        log.info(principal.getName());
//
//        if(!AdmService.(principal.getName()).getUserRole().toString().equals("ADMIN")){
//            return "redirect:/";
//        }
//
//        PageResponseDTO<UserDTO> responseDTO = userService.list(pageRequestDTO);
//        model.addAttribute("pageResponseDTO", responseDTO);
//
//        return "/adm/list";
//    }


}
