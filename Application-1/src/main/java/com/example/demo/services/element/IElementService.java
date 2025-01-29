package com.example.demo.services.element;

import com.example.demo.entities.Element;
import com.example.demo.entities.Module;


public interface IElementService {
	public Element creerElement(Long id,String nom,Module module);
	public Element modifierElement(Long id,String ancienNom,String nouveauNom);
	public boolean supprimerElement(Long id);

}
