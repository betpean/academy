package com.academy.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class AuditConfig {  //데이터관리나 타임스태프 같은 기능


    @Bean
    public AuditorAware<String> auditorProvider(){
        return  new AuditorAwareImpl();
    }


}
