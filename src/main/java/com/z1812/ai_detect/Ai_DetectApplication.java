package com.z1812.ai_detect;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("com.z1812.ai_detect.mapper")
public class Ai_DetectApplication {

	public static void main(String[] args) {
		SpringApplication.run(Ai_DetectApplication.class, args);
	}

}
