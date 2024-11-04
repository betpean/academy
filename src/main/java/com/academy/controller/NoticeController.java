package com.academy.controller;

import com.academy.dto.NoticeDTO;
import com.academy.dto.PageRequestDTO;
import com.academy.dto.PageResponseDTO;
import com.academy.service.NoticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeService noticeService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/upload")
    public String uploadForm(Model model) {
        model.addAttribute("noticeDTO", new NoticeDTO());
        return "notice/upload";
    }

    @PostMapping("/upload")
    public String uploadPost(@Valid NoticeDTO noticeDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, Principal principal) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/notice/upload";
        }
        Long nno = noticeService.upload(noticeDTO, principal.getName());
        redirectAttributes.addFlashAttribute("result", nno + " 공지가 등록되었습니다.");
        return "redirect:/notice/list";
    }

    @GetMapping("/modify/{nno}")
    public String modifyForm(@PathVariable Long nno, Model model) {
        NoticeDTO noticeDTO = noticeService.view(nno);
        model.addAttribute("noticeDTO", noticeDTO);
        return "notice/modify";
    }

    @PostMapping("/modify")
    public String modifyPost(@Valid NoticeDTO noticeDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/notice/modify/" + noticeDTO.getNno();
        }
        noticeService.modify(noticeDTO);
        redirectAttributes.addFlashAttribute("result", noticeDTO.getNno() + " 공지가 수정되었습니다.");
        return "redirect:/notice/list";
    }

    @GetMapping("/list")
    public String list(PageRequestDTO pageRequestDTO, Model model) {
        PageResponseDTO<NoticeDTO> responseDTO = noticeService.list(pageRequestDTO);
        model.addAttribute("pageResponseDTO", responseDTO);
        return "notice/list";
    }

    @GetMapping("/view/{nno}")
    public String view(@PathVariable Long nno, Model model) {
        NoticeDTO noticeDTO = noticeService.view(nno);
        model.addAttribute("notice", noticeDTO);
        return "notice/view";
    }

    @GetMapping("/delete/{nno}")
    public String delete(@PathVariable Long nno, RedirectAttributes redirectAttributes) {
        noticeService.delete(nno);
        redirectAttributes.addFlashAttribute("result", nno + " 공지가 삭제되었습니다.");
        return "redirect:/notice/list";
    }
}
