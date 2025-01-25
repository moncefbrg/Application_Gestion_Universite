package com.example.demo;

import com.example.demo.services.Etudiant.IEtudiantService;
import com.example.demo.services.fichierexcel.IFichierExcelService;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	  //File file = new File("C:\\Users\\MONCEF\\Desktop\\Notes_ClasseA_Session1.xlsx");
	  //System.out.println(iFichierExcelService.verifierNotesFichierExcel(file));
			  List<String> modules=new ArrayList<>(Arrays.asList("Analyse","Comptabilite","num"));
			  List<String> enseignants=new ArrayList<>(Arrays.asList("Ahmed","Adil","mounir"));
			  List<String> etudiants=new ArrayList<>(Arrays.asList("Ali","Hamid"));
			  iFichierExcelService.createExcelFile("C:\\Users\\MONCEF\\Desktop\\", "2024/2025", "25/01/2025", "G1", modules, enseignants, etudiants);
	  }; }}
	 
