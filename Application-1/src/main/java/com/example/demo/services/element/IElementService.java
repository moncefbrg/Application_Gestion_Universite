package com.example.demo.services.element;

import java.util.List;
import java.util.Optional;

import com.example.demo.entities.Element;
import com.example.demo.entities.Module;


public interface IElementService {
	public Element creerElement(Long id,String nom,Module module);
	public Element modifierElement(Long id,String ancienNom,String nouveauNom);
	public boolean supprimerElement(Long id);
	public List<Element> getElementsByModule(Module module);
	public List<Element> getAllElements();
	public Optional<Element> findById(Long id);

}
