package com.example.demo;

import com.example.demo.repositories.INiveau;
import com.example.demo.security.entities.Personne;
import com.example.demo.security.entities.Role;
import com.example.demo.security.repositories.IPersonne;
import com.example.demo.security.repositories.IRole;
import com.example.demo.security.repositories.IUtilisateur;
import com.example.demo.security.services.Utilisateur.UtilisateurService;
import com.example.demo.services.Etudiant.IEtudiantService;
import com.example.demo.services.classe.IClasseService;
import com.example.demo.services.element.IElementService;
import com.example.demo.services.fichierexcel.IFichierExcelService;
import com.example.demo.services.formule.IFormuleService;
import com.example.demo.services.module.IModuleService;
import com.example.demo.services.niveau.INiveauService;
import com.example.demo.services.seuil.ISeuilService;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;


@EnableScheduling
@SpringBootApplication
public class Application1Application implements CommandLineRunner {

	@Autowired
	private IUtilisateur iutilisateur;
	
	@Autowired
	private UtilisateurService utilisateurService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private IRole iRole;
	
	@Autowired
	private IPersonne iPersonne;

    public static void main(String[] args) {
        SpringApplication.run(Application1Application.class, args);
    }
   
	
	  @Bean CommandLineRunner demo(IEtudiantService iEtudiantService,
			  INiveauService iNiveauService,
			  IModuleService iModuleService,
			  IElementService iElementService,
			  ISeuilService iSeuilService,
			  IFormuleService iFormuleService,
			  IFichierExcelService iFichierExcelService,
			  INiveau iNiveau,
			  IClasseService iClasseService
			  ) {
		  return args-> { 

			  //Element element3=iElementService.creerElement((long)22, "Element 22");
			  //Element element4=iElementService.creerElement((long)23, "Element 23");
			  
			  //List<Element> elements2=Arrays.asList(element3,element4);
				/*
				 * List<String> param =Arrays.asList("G5","H5"); Seuil
				 * seuil=iSeuilService.creerSeuil((long)1,12, 10); Niveau
				 * niveau=iNiveauService.creerNiveau((long)1, "genie logiciel", "GLCC", null,
				 * seuil); iFormuleService.creerFormule((long)1, "Moyenne", "=AVG",
				 * param,niveau); Module module=iModuleService.creerModule((long)17,
				 * "Informatique", "S3",niveau,null); iElementService.creerElement((long)20,
				 * "Element 20",module); iElementService.creerElement((long)21,
				 * "Element 21",module); iClasseService.creerClasse("classeA");
				 * iEtudiantService.ajouterEtudiant((long)12, "Ahmed", "nacir", "E122890",
				 * "GLCC", null); iEtudiantService.creationFichierNoteExcel(String classe,String
				 * session,String enseignant,String semestre,String module, path);
				 */
			  //File file=new File("C:\\Users\\MONCEF\\Desktop\\projet.xlsx");
			  //String path="C:\\Users\\MONCEF\\Desktop";
			  //String date="29/01/2025";
			  //iEtudiantService.inscrire(file);
			 // iFichierExcelService.creationFichierDeliberation(path,date, "M11");
			  //iEtudiantService.modifierEtudiant((long)1, null, null, "LUCY", null);
			  //iClasseService.consulterClasse("M11");
	  }; }

	  @Override
	  public void run(String... args) throws Exception {
	      // Vérifier si le rôle ADMIN_USER existe déjà
	      if (!iRole.existsByNom("ADMIN_USER")) {
	          // Créer le rôle ADMIN_USER
	          Role adminRole = new Role();
	          adminRole.setNom("ADMIN_USER");
	          iRole.save(adminRole);
	          System.out.println("Rôle ADMIN_USER créé avec succès !");
	      }

	      // Vérifier si un compte ADMIN_USER existe déjà
	      if (!iutilisateur.existsByUsername("admin")) {
	          // Créer une Personne pour l'utilisateur admin
	          Personne adminPersonne = new Personne();
	          adminPersonne.setId((long) 1); // Utiliser le même ID que l'utilisateur
	          adminPersonne.setNom("Admin");
	          adminPersonne.setPrenom("User");
	          adminPersonne.setCin("ADMIN123");
	          adminPersonne.setEmail("admin@example.com");
	          adminPersonne.setTelephone("123456789");

	          // Enregistrer la Personne dans la base de données
	          iPersonne.save(adminPersonne);

	          // Créer un compte ADMIN_USER
	          utilisateurService.creerUtilisateur(
	                  (long) 1,
	                  "admin",
	                  "admin123", // Mot de passe en clair
	                  "ADMIN_USER"
	          );
	          System.out.println("Compte ADMIN_USER créé avec succès !");
	      } else {
	          System.out.println("Un compte ADMIN_USER existe déjà.");
	      }
	  }
	  
	  
	  
}
	 
