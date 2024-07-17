package com.academy.controller;

import com.academy.dto.*;
import com.academy.entity.User;
import com.academy.service.AdmBoardImgService;
import com.academy.service.AdmService;
import com.academy.service.BoardImgService;
import com.academy.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
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
@RequestMapping("/adm")
public class AdmController {

    private final AdmBoardImgService admBoardImgService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final AdmService admService;


    @GetMapping("/register")
    public String admregister(Model model, UserFormDTO userFormDTO) {

        model.addAttribute("userFormDTO", userFormDTO);

        return "adm/register";
    }

    @PostMapping("/register")
    public String admregister(@Valid UserFormDTO userFormDTO,
                              BindingResult bindingResult, Model model) {

        log.info("졸려" + userFormDTO);
        if (bindingResult.hasErrors()) {
            log.info("졸려2");
            return "adm/register";
        }

        try {
            User adm = User
                    .createAcademy(userFormDTO, passwordEncoder);

            userService.saveUser(adm);

        } catch (IllegalStateException e) {


            model.addAttribute("errorMessage", e.getMessage());
            return "adm/register";
        }
        log.info("졸려1");
        return "redirect:/user/login";

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/upload")
    public void upload(Principal principal, Model model) {
        log.info(principal.getName());
    }

    @PostMapping("/upload")
    public String uploadPost(@Valid AdmBoardDTO admboardDTO,
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
            return "redirect:/adm/upload";
        }

        Long abno = admService.upload(admboardDTO, principal.getName());
        admBoardImgService.register(abno, boardImgFileList);

        redirectAttributes.addFlashAttribute("result", abno + "글이 등록되었습니다.");

        return "redirect:/adm/list";
    }

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model) {
        PageResponseDTO<AdmBoardDTO> responseDTO = admService.list(pageRequestDTO);
        log.info(responseDTO);
        model.addAttribute("pageResponseDTO", responseDTO);

    }

    @GetMapping(value = "/view")
    public String boardview(Model model, PageRequestDTO pageRequestDTO, Long abno,Principal principal) {
        AdmBoardDTO admboardDTO = admService.view(abno, pageRequestDTO,principal);
        model.addAttribute("adto", admboardDTO);
        model.addAttribute("imgdto1", admBoardImgService.admimglist(abno));

        if(principal == null){
            return "redirect:/user/login";
        }
        log.info("글번호 : " + abno);
        if (admboardDTO != null) {
            log.info("아이디는: " + admboardDTO.getAdu());
        } else {
            log.info("삐");
        }

        return "adm/view";
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{abno}")
    public String boardmodify(@PathVariable Long abno, Model model, PageRequestDTO pageRequestDTO,Principal principal) {
        AdmBoardDTO admBoardDTO = admService.view(abno, pageRequestDTO,principal);
        model.addAttribute("adto", admBoardDTO);
        model.addAttribute("imgdto", admBoardImgService.admimglist(abno));

        return "/adm/modify";
    }

    @PostMapping("/modify")
    public String boardmodifypost(@Valid AdmBoardDTO admBoardDTO,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes,
                                  PageRequestDTO pageRequestDTO,Long[] ino,
                                  @RequestParam("boardImgFile") List<MultipartFile> boardImgFileList) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes
                    .addAttribute("abno", admBoardDTO.getAbno());
            return "redirect:/adm/modify?" + pageRequestDTO.getLink();
        }
        admService.modify(admBoardDTO);
        if( ino != null && ino.length > 0){
            admBoardImgService.modify(ino);
        }
        admBoardImgService.register(admBoardDTO.getAbno(), boardImgFileList);
        redirectAttributes.addAttribute("abno", admBoardDTO.getAbno());
        redirectAttributes.addFlashAttribute("result", admBoardDTO.getAbno() + "글이 수정되었습니다.");
        return "redirect:/adm/view?" + pageRequestDTO.getLink();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete")
    public String boarddelete(Long abno, RedirectAttributes redirectAttributes) {

        admService.delete(abno);
        redirectAttributes.addFlashAttribute("result", abno + " 번 글이 삭제되었습니다.");

        return "redirect:/adm/list";
    }

    @PostMapping("/file")
    public String filePost(AdmBoardDTO admBoardDTO, @RequestParam("file") MultipartFile[] file) {

        if (file != null && file.length > 0) {

            for (int i = 0; i < file.length; i++) {
                log.info(file[i].getContentType());
                log.info(file[i].getOriginalFilename());

                String path = "c:\\aaa\\" + file[i].getOriginalFilename();


                File save = new File(path);

                try {

                    file[i].transferTo(save);

                } catch (Exception e) {

                }

            }


        }
        return null;

    }
}