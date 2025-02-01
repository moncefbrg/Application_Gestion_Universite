package com.example.demo.services.niveau;

import java.util.List;

import com.example.demo.entities.Formule;
import com.example.demo.entities.Niveau;
import com.example.demo.entities.Seuil;
import com.example.demo.entities.Module;

public interface INiveauService {
	public Niveau creerNiveau(Long id,String nom,String alias,Niveau niveauSuivant,Seuil seuil)throws Exception;
	public boolean modifierNiveau(Long id,String nom,String alias,Niveau niveauSuivant,List<Module> modules,Seuil seuil,List<Formule> formules)throws Exception;
	public boolean supprimerNiveau(String nom);
	public boolean checkExistanceNiveau(Long id);
	public boolean associerSeuilNiveau(Niveau niveau,Seuil seuil)throws Exception;
	public boolean separerSeuilNiveau(Niveau niveau,Seuil seuil)throws Exception;
	public List<Niveau> getAllNiveaux();
	public Niveau getNiveauById(Long id); 

}
