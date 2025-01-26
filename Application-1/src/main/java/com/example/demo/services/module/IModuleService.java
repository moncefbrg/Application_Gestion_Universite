package com.example.demo.services.module;

import java.util.List;

import com.example.demo.entities.Element;

public interface IModuleService {
	public boolean creerModule(Long id,String nom,String  semestre,List<Element> elements);
	public boolean modifierModule(Long id,String nom,String  semestre,List<Element> elements);
	public boolean supprimerModule(Long id);
}
