package com.example.demo;

import com.example.demo.services.Etudiant.IEtudiantService;
import com.example.demo.services.fichierexcel.IFichierExcelService;

import java.io.File;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;



@SpringBootApplication
public class Application1Application {

    public static void main(String[] args) {
        SpringApplication.run(Application1Application.class, args);
    }
   
	
	  @Bean CommandLineRunner demo(IEtudiantService iEtudiantService,IFichierExcelService iFichierExcelService) {
		  return args-> { 
	  File file = new File("C:\\Users\\MONCEF\\Desktop\\Notes_ClasseA_Session1.xlsx");
	  //etudiantService.inscrire(file);
				/*
				 * File file=etudiantService.creationFichierNoteExcel("ClasseA", "Session1",
				 * "M. Dupont", "Semestre1", "Mathématiques",
				 * "C:\\\\Users\\\\MONCEF\\\\Desktop"); if (file != null) {
				 * System.out.println("Fichier créé avec succès : " + file.getAbsolutePath()); }
				 * else { System.out.println("Erreur lors de la création du fichier."); }
				 */
	  //iFichierExcelService.verifierFormatFichierExcel(file);
	  System.out.println(iFichierExcelService.verifierNotesFichierExcel(file));
	  }; }}
	 
