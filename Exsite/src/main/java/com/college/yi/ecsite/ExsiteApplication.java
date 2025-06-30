package com.college.yi.ecsite;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.college.yi.ecsite.front.repository")
public class ExsiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExsiteApplication.class, args);
	}

}
