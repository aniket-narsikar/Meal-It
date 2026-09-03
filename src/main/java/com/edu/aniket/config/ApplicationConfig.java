package com.edu.aniket.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class ApplicationConfig {

	@Bean
	public OpenAPI swaggerDocOpenApi() {
		Server currentServer = new Server();
		currentServer.setUrl("/");
		currentServer.setDescription("Current Server (Auto-detected)");

		Server devlopmentServer = new Server();
		devlopmentServer.setUrl("http://localhost:8081");
		devlopmentServer.setDescription("Local Development Server");

		Server productionServer = new Server();
		productionServer.setUrl("https://meal-it-production.up.railway.app");
		productionServer.setDescription("Cloud Production Server");

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

		SecurityScheme securityScheme = new SecurityScheme()
				.name("Bearer Authentication")
				.type(SecurityScheme.Type.HTTP)
				.bearerFormat("JWT")
				.scheme("bearer");

		SecurityRequirement securityRequirement = new SecurityRequirement().addList("Bearer Authentication");

		OpenAPI openAPI = new OpenAPI();
		openAPI.info(info);
		openAPI.servers(Arrays.asList(currentServer, productionServer, devlopmentServer));
		openAPI.addSecurityItem(securityRequirement);
		openAPI.components(new Components().addSecuritySchemes("Bearer Authentication", securityScheme));

		return openAPI;
	}
}
