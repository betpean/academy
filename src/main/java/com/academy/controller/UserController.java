package com.academy.controller;

import com.academy.dto.PageRequestDTO;
import com.academy.dto.PageResponseDTO;
import com.academy.dto.UserFormDTO;
import com.academy.entity.User;
import com.academy.service.UserService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Getter
@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;



    @PostMapping("/name")
    public @ResponseBody String findname(String email){

        log.info(email);
        User user = userService.findByEmail(email);
        log.info(user);
        if (user == null){
            return "";
        }else {
            return user.getName();
        }


    }

    @GetMapping("/new")
    public String register(Model model){      //object로 보내서 from에서 사용하려고

        model.addAttribute("userFormDTO", new UserFormDTO());

        return "user/register";    //view > html

    }


    @PostMapping("/new")
    public String register(@Valid UserFormDTO userFormDTO,
                             BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()){
            return "user/register";
        }

        try {
            User user = User
                    .createUser(userFormDTO, passwordEncoder);

            userService.saveUser(user);

        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "user/register";
        }
        return "redirect:login";
    }



    @GetMapping("/login")
    public String loginUser(){

        return "/user/login";
    }
    @GetMapping("/login/error")
    public String loginError(Model model){

        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");

        return "/user/login";
    }
    @GetMapping("/list")
    public String list(PageRequestDTO pageRequestDTO, Model model, Principal principal) {



        if(principal == null){
            return "redirect:/";
        }
        log.info(principal.getName());

        if(!userService.findByEmail(principal.getName()).getRole().toString().equals("ADMIN")){
            return "redirect:/";
        }

        PageResponseDTO<UserFormDTO> responseDTO = userService.list(pageRequestDTO);
        model.addAttribute("pageResponseDTO", responseDTO);

        return "/user/list";
    }
    @GetMapping("/delete")
    public String regination(){
        return "/user/delete";
    }
    @GetMapping("/myinfo")
    public String myinfo(Principal principal , Model model){

        String email = principal.getName();
        UserFormDTO userFormDTO = userService.findUserByEmail(email);
        model.addAttribute("user1", userFormDTO);


        return "/user/myinfo";
    }
    @GetMapping("/view")
    public String view(PageRequestDTO pageRequestDTO, Model model, Principal principal) {
        log.info(principal.getName());

        if(principal == null){
            return "redirect:/";
        }
        log.info(principal.getName());

        if(!userService.findByEmail(principal.getName()).getRole().toString().equals("ADMIN")){
            return "redirect:/";
        }
        log.info(principal.getName());


        PageResponseDTO<UserFormDTO> responseDTO = userService.view(pageRequestDTO);
        model.addAttribute("pageResponseDTO", responseDTO);
        return "/user/view";
    }
    @GetMapping("/modify")
    public String getmyinfo(Principal principal, Model model) {
        String email = principal.getName();
        UserFormDTO userFormDTO = userService.findUserByEmail(email);
        model.addAttribute("user", userFormDTO);
        return "/user/modify";
    }

    @PostMapping("/modify")
    public String updateMyinfo(Principal principal, UserFormDTO userFormDTO, Model model) {
        String email = principal.getName();


        try {
            userService.updateUser(email, userFormDTO, passwordEncoder);    //3개의 값을 담아서 updateUser로 보내줌
//      userFormDTO: 사용자 정보를 담고 있는 데이터 전송 객체
//      업데이트할 사용자의 '새로운' 정보를 포함 함
        } catch (IllegalStateException e) {         //예외처리 일치화
            model.addAttribute("errorMessage", e.getMessage());
            return "/user/modify";
        }
//        model.addAttribute("user", userFormDTO); // dto가 기존의 값을 저장하고있음
        return "redirect:/user/myinfo"; //수정된값을 가지고 바로 info페이지로 돌아감
    }










}
