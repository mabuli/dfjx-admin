package io.dfjx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("io.dfjx.dao")
public class DfjxGenApplication {

	public static void main(String[] args) {
		SpringApplication.run(DfjxGenApplication.class, args);
	}
}
