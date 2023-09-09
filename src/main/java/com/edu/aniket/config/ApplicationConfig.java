package com.edu.aniket.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class ApplicationConfig {
	@Bean
	public OpenAPI swaggerDocOpenApi() {
		Server devlopmentServer = new Server();
		devlopmentServer.setUrl("http://localhost:8081");
		devlopmentServer.setDescription("This Url is For Development");

		Server productionServer = new Server();
		productionServer.setUrl("http://localhost:8081");
		productionServer.setDescription("This Url is For Production");

		Contact contact = new Contact();
		contact.setName("MealIt");
		contact.setEmail("info.mealit.in");
		contact.setUrl("www.mealit.in");

		License license = new License();
		license.name("MIT License");
		license.url("License Url Coming Soon!! ");

		Info info = new Info();
		info.title("Meal It RestApi's");
		info.version("1.0.0");
		info.contact(contact);
		info.description("This application is desined for FoodApp to Avoid the Manual Work");
		info.termsOfService("www.mealit.in");
		info.license(license);

		OpenAPI openAPI = new OpenAPI();
		openAPI.info(info);
		openAPI.servers(Arrays.asList(devlopmentServer, productionServer));

		return openAPI;
	}
}
