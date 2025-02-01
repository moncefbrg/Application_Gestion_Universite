package com.example.demo.services.seuil;

import java.util.List;

import com.example.demo.entities.Seuil;

public interface ISeuilService {
	public Seuil creerSeuil(Long id,double xNormale,double yRattrapage)throws Exception;
	public Seuil modifierSeuil(Long id,Double nXNormale,Double nYRattrapage);
	public boolean supprimerSeuil(Long id);
	public List<Seuil> getAllSeuils();
	public Seuil getSeuilById(Long id);

}
