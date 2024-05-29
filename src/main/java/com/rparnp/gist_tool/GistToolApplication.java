package com.rparnp.gist_tool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GistToolApplication {

	public static void main(String[] args) {
		SpringApplication.run(GistToolApplication.class, args);
	}

}
