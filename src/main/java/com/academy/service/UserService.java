package com.academy.service;

import com.academy.constant.Role;
import com.academy.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework. stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public com.academy.entity.User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    // 회원가입
    public com.academy.entity.User saveUser(com.academy.entity.User user) {
        log.info("사용자가 있는지 확인하기 전");

        // 사용자가 이미 있는지 찾기
        validateDuplicateuser(user);

        log.info("가입된 사용자가 없어서 저장하기 전");
        
        return userRepository.save(user);

    }

    private void validateDuplicateuser(com.academy.entity.User user) {
        log.info("이미 가입된 회원인지 확인");
        // 데이터베이스에 저장된 회원가입이 되어있는지 찾아본다.
        com.academy.entity.User finduser = userRepository.findByEmail(user.getEmail());

        //이미 가입된 email이라면
        if(finduser != null) {
            log.info("이미 가입된 회원");
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }

    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.academy.entity.User user = this.userRepository.findByEmail(email);

        if(user == null){
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다." + email);
        }
//        User user1 = user.get();  optional 이라서 했던 코드
//        List<GrantedAuthority> authorities = new ArrayList<>(); // 권한들
        log.info(user);
        log.info("현재 로그인하신분의 권한 : " +user.getRole().name());
        String role  = "";
        if("ADMIN".equals(user.getRole().name())){   //auth 컬럼을 추가로 지정해서 사용
            log.info("관리자");
            role = Role.ADMIN.name();
//            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.name()));
        }else {
            log.info("일반유저");
            role = Role.USER.name();
           // authorities.add(new SimpleGrantedAuthority(Role.USER.name()));
        }


        return User.builder()
                .username( user.getEmail())
                .password( user.getPassword())
                .roles(role)
                .build();
                //User(memberuser.getEmail(), memberuser.getPassword(), authorities);
        // 권한 까지 부여된후 User객체를 생성해 반환함, 스프링시큐리티에서 사용하며, 아이디, 패스워드, 권한 을 리턴
        // 리턴된 User객체의 비밀번호와 , 사용자가 폼에 입력한 비밀번호가 일치하는지 검사하는 기능을 내부에 가지고 있음
        //여러 롤을 배열로 던질것인가 생각
    }
}
