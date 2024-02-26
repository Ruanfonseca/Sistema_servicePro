package com.ServicePro.ServicePro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.ServicePro.ServicePro.mensageria")
public class ServiceProApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceProApplication.class, args);
    }

}
