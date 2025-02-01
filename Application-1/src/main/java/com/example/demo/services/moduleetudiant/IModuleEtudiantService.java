package com.example.demo.services.moduleetudiant;


import com.example.demo.entities.Etudiant;
import com.example.demo.entities.Module;


public interface IModuleEtudiantService {
	public boolean associerEtudiantModule(Etudiant etudiant,Module module)throws Exception;
	public boolean separerEtudiantModule(Etudiant etudiant,Module module)throws Exception;
	
}
