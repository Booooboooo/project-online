/**
 * 
 */
/**
 * @author chris
 *
 */
package com.nais.kid.keeper;

import java.util.Arrays;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;


@MapperScan(value="com.nais.mapper")
@ComponentScan(value={"com.nais.kid.keeper.controller",
		"com.nais.service", "com.nais.kid.keeper.config", "com.nais.kid.keeper.service"})
@EnableAutoConfiguration
@SpringBootApplication
public class Application {
	
	public static void main(String[] args) {
		
        SpringApplication.run(Application.class, args);
    }
	
	@Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            System.out.println("Let's inspect the beans provided by Spring Boot:");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }

        };
    }
	
}