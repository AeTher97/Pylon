package org.urzednicza.pylon;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.urzednicza.pylon.services.ApplicationService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@SpringBootApplication
public class Pylon {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {return new BCryptPasswordEncoder();}

    public static void main(String[] args) {
        SpringApplication.run(Pylon.class,args);
    }
}
