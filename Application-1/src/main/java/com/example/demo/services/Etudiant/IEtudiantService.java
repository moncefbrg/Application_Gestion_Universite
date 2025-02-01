package com.example.demo.services.Etudiant;

import java.io.File;
import java.util.List;
import java.util.Optional;

import com.example.demo.entities.Classe;
import com.example.demo.entities.Etudiant;
public interface IEtudiantService {
	public boolean inscrire(File fichier)throws Exception;
	public Etudiant chercherEtudiantById(Long id);//done
	public List<Etudiant> chercherEtudiant(String cne,String nom,String prenom,Long niveau);//done
	public boolean checkNiveau();//results
	public boolean inscription(File fichier,List<String> liste,int nbrColonnes);//done
	public boolean modifierEtudiant(Long id,String cne,String nom,String prenom,Long niveau);//done+
	public boolean creerEtSauvegarderEtudiantFromExcel(List<String> ligneData);//done
	public File exporter();
	public boolean ajouterEtudiant(Long id,String nom,String prenom,String cne,String aliasNiveau,String Classe)throws Exception;//done+
	public boolean supprimerEtudiant(Long id);//done
	public boolean associerEtudiantClasse(Classe classe,Etudiant etudiant)throws Exception;
	public boolean separerEtudiantClasse(Classe classe,Etudiant etudiant)throws Exception;
	public List<Etudiant> getAllEtudiants();
	public Optional<Etudiant> findById(Long id);
}
