package com.atlantis.supermarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

//@EnableCaching
@SpringBootApplication
@EnableAsync
public class SupermarketApplication {
   
    public static void main(String[] args) {
	SpringApplication.run(SupermarketApplication.class, args);
    }

}
