package com.academy.service;

import com.academy.constant.Role;
import com.academy.dto.UserFormDTO;
import com.academy.dto.PageRequestDTO;
import com.academy.dto.PageResponseDTO;
import com.academy.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework. stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


    public com.academy.entity.User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // 회원가입
    public com.academy.entity.User saveUser(com.academy.entity.User user) {
        log.info("사용자가 있는지 확인하기 전");

        // 사용자가 이미 있는지 찾기
        validateDuplicateuser(user);

        log.info("가입된 사용자가 없어서 저장하기 전");
        log.info(user);

        return userRepository.save(user);

    }

    private void validateDuplicateuser(com.academy.entity.User user) {
        log.info("이미 가입된 회원인지 확인");
        // 데이터베이스에 저장된 회원가입이 되어있는지 찾아본다.
        com.academy.entity.User finduser = userRepository.findByEmail(user.getEmail());

        //이미 가입된 email이라면
        if (finduser != null) {
            log.info("이미 가입된 회원");
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }

    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.academy.entity.User user = this.userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다." + email);
        }
//        User user1 = user.get();  optional 이라서 했던 코드
//        List<GrantedAuthority> authorities = new ArrayList<>(); // 권한들
        log.info(user);
        log.info("현재 로그인하신분의 권한 : " + user.getRole().name());
        String role = "";
        if ("ADMIN".equals(user.getRole().name())) {   //auth 컬럼을 추가로 지정해서 사용
            log.info("관리자");
            role = Role.ADMIN.name();
//            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.name()));
        }else if ("ACADEMY".equals(user.getRole().name())){
            log.info("학원유저");
            role = Role.ACADEMY.name();
            // authorities.add(new SimpleGrantedAuthority(Role.USER.name()));
        }
        else {
            log.info("일반유저");
            role = Role.USER.name();
            // authorities.add(new SimpleGrantedAuthority(Role.USER.name()));
        }


        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(role)
                .build();
        //User(memberuser.getEmail(), memberuser.getPassword(), authorities);
        // 권한 까지 부여된후 User객체를 생성해 반환함, 스프링시큐리티에서 사용하며, 아이디, 패스워드, 권한 을 리턴
        // 리턴된 User객체의 비밀번호와 , 사용자가 폼에 입력한 비밀번호가 일치하는지 검사하는 기능을 내부에 가지고 있음
        //여러 롤을 배열로 던질것인가 생각
    }

    public PageResponseDTO<UserFormDTO> list(PageRequestDTO pageRequestDTO) {
        Page<com.academy.entity.User> userPage = userRepository.findAll(pageRequestDTO.getPageable());
        //userPage에 userReposigtory를 사용해서 pageRequestDTO 안의 Pageable에서 모든걸 찾아온걸 담아줌(user배열타입)
        List<UserFormDTO> userdDTOList =
                userPage.getContent().stream()
                        .map(user -> modelMapper.map(user, UserFormDTO.class))
                        .collect(Collectors.toList());

        log.info(userdDTOList); //boardDTOList에는 bno등은 있지만 userId는 없다. user.userId라서


        return PageResponseDTO.<UserFormDTO>withAll()
                .dtoList(userdDTOList)
                .pageRequestDTO(pageRequestDTO)
                .total((int) userPage.getTotalElements())
                .build();
    }

    public PageResponseDTO<UserFormDTO> view(PageRequestDTO pageRequestDTO) {
        Page<com.academy.entity.User> userPage = userRepository.findAll(pageRequestDTO.getPageable());

        List<UserFormDTO> userDTOList = userPage.getContent().stream()
                .map(user -> modelMapper.map(user, UserFormDTO.class))
                .collect(Collectors.toList());

        log.info(userDTOList);


        return PageResponseDTO.<UserFormDTO>withAll()
                .dtoList(userDTOList)
                .pageRequestDTO(pageRequestDTO)
                .total((int) userPage.getTotalElements())
                .build();
    }

    public UserFormDTO findUserByEmail(String email) {
        com.academy.entity.User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다. " + email);    //직접처리
        }
        return modelMapper.map(user, UserFormDTO.class);
    }

    public void updateUser(String email, UserFormDTO userFormDTO, PasswordEncoder PasswordEncoder) {    //페스워드엔코딩 받아주기
        com.academy.entity.User user = userRepository.findByEmail(email);   //데이터베이스에서 주어진 이메일과 일치하는 사용자 정보를 가져와서 user 변수에 넣음
        if (user == null) {
            throw new IllegalStateException("사용자를 찾을 수 없습니다. " + email);
        }
        if (user != null) {
//            user.setName(userFormDTO.getName());
            user.setPassword(passwordEncoder.encode(userFormDTO.getPassword()));
            //입력된 패스워드를 엔코딩해서 새로이 set 해줌( user가  null이 아니라면)
        }
    }
}




