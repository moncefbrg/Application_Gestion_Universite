package com.example.demo.services.classe;

import java.util.List;

import com.example.demo.entities.Classe;
import com.example.demo.entities.Etudiant;
import com.example.demo.entities.Niveau;

public interface IClasseService {
	public Classe creerClasse(String nom);
	public boolean supprimerClasse(String nom);
	public boolean modifierClasse(String nom,String nouveauNom);
	public boolean ajouterEtudiantClasse(String nom,Long id);
	public boolean supprimerEtudiantClasse(String nom,Long id);
	public List<Etudiant> consulterClasse(String nom);
	public boolean associerClasseNiveau(Classe classe,Niveau niveau)throws Exception;
	public boolean separerClasseNiveau(Classe classe,Niveau niveau)throws Exception;
	public List<Etudiant> getTousLesEtudiants();
	public List<Classe> getAllClasses();
	public Classe getClasseById(Long id);
	public Classe getClasseByNom(String nom);
	public void save(Classe classe);
	
	
}
