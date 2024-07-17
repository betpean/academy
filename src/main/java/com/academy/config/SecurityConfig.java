package com.academy.config;


import jakarta.servlet.annotation.WebListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@WebListener
public class SecurityConfig  {


    @Bean
    SecurityFilterChain filterChain(HttpSecurity http)throws  Exception{

        http
                .authorizeHttpRequests(
                        (authorizeHttpRequests) -> authorizeHttpRequests
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/notice/modify").hasRole("ADMIN")

//                                .requestMatchers(HttpMethod.POST, "/user/myinfo").permitAll()

//                                .requestMatchers("/user/list").authenticated()

//                                .requestMatchers(new AntPathRequestMatcher("/admin/**")).hasRole("AMDIM")
                                .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                                .requestMatchers("/**").permitAll()


                )
               .exceptionHandling( a -> a.accessDeniedHandler(new CustomAccessDeniedHandler()))



                //csrf 토큰은 서버에서 생성되는 임의 값으로 페이지 요청시 항상 다른값으로 생성된다.
                //토큰이란 요청을 식별하고 검증하는데 사용하는 특수한 문자열 도는 값
                //세션이란 사용자의 상태를 유지하고 관리하는데 사용하는 기능
                //미지정시 csrf의 방어기능에 의해 접근 거부
                .csrf((csrf) -> csrf

                        .disable()
                )

//                .csrf( (csrf) -> csrf
//                .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")))

                //현재 모든 경로에 대해서 csrf 미사용

                .formLogin( formLogin -> formLogin.loginPage("/user/login")
                        .defaultSuccessUrl("/")
                        .usernameParameter("email")
                        .failureUrl("/user/login/error")

                )

                .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true))


                // 로그인이 되지 않은 사용자 가 로그인을 요하는 페이지 접속시 (rest) 핸들링
                .exceptionHandling(
                        a -> a.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                )

        ;



        return http.build();

    }


    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //  스프링 시큐리티의 인증 처리 , 인증은 로그인
    // AuthenticationManager는 사용자 인증시 앞에서 작성한 UserSecurityService 와 passwordEncoder를
    //내부적으로 사용 인증과 권한 부여
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


}
