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

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @GetMapping("/new")
    public String adminregister(Model model) {
        model.addAttribute("userFormDTO", new UserFormDTO());
        return "admin/register";

    }

    @PostMapping("/new")
    public String register(@Valid UserFormDTO userFormDTO,
                           BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "admin/register";
        }
        log.info("나이2");

        try {
            User user = User
                    .createUser(userFormDTO, passwordEncoder);

            userService.saveUser(user);
            log.info("나이1");
        } catch (IllegalStateException e) {
            log.info("나이");
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/register";
        }
        return "redirect:/user/login";
    }
}
