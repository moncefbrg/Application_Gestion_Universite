package com.example.demo.services.Etudiant;

import java.io.File;
import java.util.List;
import com.example.demo.entities.Etudiant;
public interface IEtudiantService {
	public boolean inscrire(File fichier);
	public boolean checkFormat(File fichier,List<String> liste) throws Exception;//done
	public Etudiant chercherEtudiantById(Long id);//done
	public List<Etudiant> chercherEtudiant(String cne,String nom,String prenom,Long niveau);//done
	public boolean checkExistanceNiveau(Long id);//done
	public boolean checkNiveau();//results
	public boolean inscription(File fichier,List<String> liste);
	public Etudiant modifierEtudiant(Long id,String cne,String nom,String prenom,Long niveau);
	public List<Etudiant> consulterClasse();
	public File exporter();
	
	

}
