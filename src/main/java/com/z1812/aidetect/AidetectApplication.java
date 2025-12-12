package com.z1812.aidetect;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("com.z1812.aidetect.mapper")
public class AidetectApplication {

	public static void main(String[] args) {
		SpringApplication.run(AidetectApplication.class, args);
	}

}
