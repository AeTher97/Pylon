package org.urzednicza.pylon;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.urzednicza.pylon.services.ApplicationService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@SpringBootApplication
public class Pylon {



    public static void main(String[] args) {
        SpringApplication.run(Pylon.class,args);
    }
}
