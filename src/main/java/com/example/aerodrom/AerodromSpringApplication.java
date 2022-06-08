package com.example.aerodrom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("model")
public class AerodromSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(AerodromSpringApplication.class, args);
	}

}
