package com.example.demo.services.resultatelement;

import java.util.List;

import com.example.demo.entities.Element;
import com.example.demo.entities.Etudiant;
import com.example.demo.entities.ResultatElement;

public interface IResultatElementService {
	public boolean associerNoteElementEtudiant(Double note,Element element,Etudiant etudiant,String session)throws Exception;
	public boolean modifierNoteElementEtudiant(Long resultatId, Double nouvelleNote, Element element, Etudiant etudiant) throws Exception;
	public List<ResultatElement> getAllResultats();


}
