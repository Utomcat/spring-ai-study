package com.ranyk.spring.ai.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.ranyk.spring.ai.base"})
public class SpringAiBaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringAiBaseApplication.class, args);
	}

}
