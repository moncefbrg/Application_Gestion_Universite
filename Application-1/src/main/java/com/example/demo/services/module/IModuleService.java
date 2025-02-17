package com.example.demo.services.module;

import java.util.List;

import com.example.demo.entities.Element;
import com.example.demo.entities.Enseignant;
import com.example.demo.entities.Module;
import com.example.demo.entities.Niveau;

public interface IModuleService {
	public Module creerModule(Long id,String nom,String  semestre,Niveau niveau,Enseignant enseignant);
	public boolean modifierModule(Long id,String nom,String  semestre,List<Element> elements);
	public boolean supprimerModule(Long id);
	public void associerModuleEnseignant(Module module,Enseignant enseignant) throws Exception;
	public List<Module> getAllModules();
	public List<Module> getModulesByIds(List<Long> ids);
	public Module getModuleById(Long id) throws Exception;

}
