package com.springboot.ecommerce;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringbootEcommerceApplication {

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringbootEcommerceApplication.class, args);
	}

}

//Refactor #1:
//1. OK: Final
//2. OK: Valid for requestbody
//3. OK: Security move to config
//4. OK: Pathvariable into parameter


//Refactor #2:
//1. No: Category - Brand: @ManyToMany
//2. No: Add liquibase
//