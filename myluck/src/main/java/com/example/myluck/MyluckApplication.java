package com.example.myluck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;
@SpringBootApplication
@MapperScan("com.example.myluck.dao")
public class MyluckApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyluckApplication.class, args);
	}

}
