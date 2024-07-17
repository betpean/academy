package com.academy.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/admin1")
public class AdminController {



    @GetMapping("/detail")
    public String totalinfo(){
        return "/admin/detail";
    }

    @GetMapping("/admdetail")
    public String adminfo(){
        return "/admin/admdetail";
    }

}
