package com.example.demo;

import com.example.demo.services.Etudiant.IEtudiantService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;



@SpringBootApplication
public class Application1Application {

    public static void main(String[] args) {
        SpringApplication.run(Application1Application.class, args);
    }
   
	
	  @Bean CommandLineRunner demo(IEtudiantService etudiantService) {
		  return args-> { 
	  //File file = new File("C:\\Users\\MONCEF\\Desktop\\projet.xlsx");
	  //etudiantService.inscrire(file);
	  }; }}
	 
