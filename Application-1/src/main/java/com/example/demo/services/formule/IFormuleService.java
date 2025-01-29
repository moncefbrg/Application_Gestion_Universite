package com.example.demo.services.formule;

import java.util.List;

import com.example.demo.entities.Formule;
import com.example.demo.entities.Niveau;

public interface IFormuleService {
	public Formule creerFormule(Long id,String nom,String expression,List<String> param,Niveau niveau);
	public Formule modifierFormule(Long id,String nouveauNom,String nouvelleExpression,List<String> nParam);
	public boolean supprimerFormule(Long id);

}
