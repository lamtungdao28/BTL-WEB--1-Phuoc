package com.ptit.thuvien;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Lớp khởi chạy ứng dụng Spring Boot
 * Kế thừa SpringBootServletInitializer để hỗ trợ deploy trên Tomcat với JSP
 */
@SpringBootApplication
@EnableScheduling
public class ThuVienApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ThuVienApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(ThuVienApplication.class, args);
    }
}
