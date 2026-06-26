package com.ranyk.spring.ai.mcp.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.ranyk.spring.ai.mcp.server")
public class SpringAiMcpServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringAiMcpServerApplication.class, args);
	}

}
