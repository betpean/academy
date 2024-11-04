package com.academy.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/reply")
public class RepliesController {

    @GetMapping("/upload")
    public String reliesupload(){
        return "/reply/upload";
    }
    @GetMapping("/delete")
    public String reliesdelete(){
        return "/reply/delete";
    }@GetMapping("/modify")
    public String reliesmodify(){
        return "/reply/modify";
    }@GetMapping("/list")
    public String relieslist(){
        return "/reply/list";
    }
}
