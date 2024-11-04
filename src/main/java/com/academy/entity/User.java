package com.academy.entity;


import com.academy.constant.Role;
import com.academy.dto.UserFormDTO;
import com.academy.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(name = "user")
public class User extends BaseEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uno;

    private String id;            //아이디

    private String password;    //비밀번호

    @Column(unique = true)
    private String email;       //이메일

    private String name;       //이름

    private String age;        //나이

    private String gender;      //성별

    private String address;     // 주소

    private String number;      // 전화번호

    @Enumerated(EnumType.STRING)
    private Role role;          //권한

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;


    //회원가입용
    public static User createUser(UserFormDTO userFormDTO,
                                  PasswordEncoder passwordEncoder) {
        //modelmapper
        User user = new User();
        user.setName(userFormDTO.getName());
        user.setAge(userFormDTO.getAge());
        user.setEmail(userFormDTO.getEmail());
        user.setGender(userFormDTO.getGender());
        //패스워드의 암호화
        String password =  passwordEncoder.encode(userFormDTO.getPassword());
        user.setPassword(password);
        user.setAddress(userFormDTO.getAddress());
        //memberuser.setRole(Role.USER);      // 사용자가 가입했을때
        user.setRole(Role.USER);      // 사용자가 가입했을때

        return user;
    }
    public static User createAdminUser(UserFormDTO userFormDTO,
                                       PasswordEncoder passwordEncoder) {

        User user = new User();
        user.setEmail(userFormDTO.getEmail());
        user.setName(userFormDTO.getName());
        String password = passwordEncoder.encode(userFormDTO.getPassword());
        user.setPassword(password);
        user.setRole(Role.ADMIN);		// Role 설정
        return user;
    }




















}
