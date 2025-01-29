package com.example.demo.services.seuil;

import com.example.demo.entities.Seuil;

public interface ISeuilService {
	public Seuil creerSeuil(Long id,double xNormale,double yRattrapage)throws Exception;
	public Seuil modifierSeuil(Long id,Double nXNormale,Double nYRattrapage);
	public boolean supprimerSeuil(Long id);

}
