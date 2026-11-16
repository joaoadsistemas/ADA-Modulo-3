package br.com.ada.antifraude;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ServicoAntifraudeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicoAntifraudeApplication.class, args);
	}

}
