package com.habimed.habimedWebService;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@OpenAPIDefinition(
		info = @Info(
				title = "API de Ejemplo",
				version = "1.0",
				description = "Documentación de la API REST"
		)
)
@SpringBootApplication
public class HabimedWebServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HabimedWebServiceApplication.class, args);
	}

}
