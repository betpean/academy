package com.academy.controller;

import com.academy.dto.UserFormDTO;
import com.academy.entity.User;
import com.academy.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

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
        log.info("나이1");
        if(bindingResult.hasErrors()){
            return "user/register";
        }log.info("나이2");

        try {
            User user = User
                    .createUser(userFormDTO, passwordEncoder);

            userService.saveUser(user);
            log.info("나이1");
        } catch (IllegalStateException e) {log.info("나이");
            model.addAttribute("errorMessage", e.getMessage());
            return "user/register";
        }




        return "redirect:/user/login";
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
    public String totallist(){
        return "/user/list";
    }
    @GetMapping("/delete")
    public String regination(){
        return "/user/delete";
    }
    @GetMapping("/modify")
    public String usermodify(){
        return "/user/modify";
    }
    @GetMapping("/myinfo")
    public String userdetail(Principal principal , Model model){

        String email = principal.getName();/*principal이용해서 본인의 이메일을 가져옴*/
        User user = userService.findByEmail(email);/*유저에 저장*/
        log.info(user);
        model.addAttribute("user", user);/*user라는 이름으로 user값 전송*/


        return "/user/myinfo";/*마이페이지로 이동~*/
    }










}
