package com.example.demo.services.resultatelement;

//log done


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.Element;
import com.example.demo.entities.Etudiant;
import com.example.demo.entities.Niveau;
import com.example.demo.entities.ResultatElement;
import com.example.demo.repositories.IElement;
import com.example.demo.repositories.IEtudiant;
import com.example.demo.repositories.IResultatElement;

@Service("ResultatElementServiceImpl")
public class ResultatElementServiceImpl implements IResultatElementService {
	@Autowired
	private IElement iElement;
	@Autowired
	private IEtudiant iEtudiant;
	@Autowired
	private IResultatElement iResultatElement;
	
	private static final Logger logger=LoggerFactory.getLogger(ResultatElementServiceImpl.class);
	@Override
	@Transactional
	public boolean associerNoteElementEtudiant(Double note, Element element, Etudiant etudiant,String session) throws Exception {
	    // Vérification des nullités
	    if (note == null) {
	        throw new RuntimeException("Vous devez entrer la note.");
	    }
	    if (session == null) {
	        throw new RuntimeException("Vous devez entrer la session.");
	    }
	    if (element == null) {
	        throw new RuntimeException("Vous devez entrer l'élément.");
	    }
	    if (etudiant == null) {
	        throw new RuntimeException("Vous devez entrer l'étudiant.");
	    }

	    // Vérification de la validité de la note
	    if (note < 0 || note > 20) {
	        throw new RuntimeException("La note doit être comprise entre 0 et 20.");
	    }
	    if (!session.equals("Normale") || !session.equals("Rattrapage") || !session.equals("normale") || !session.equals("rattrapage")) {
	        throw new RuntimeException("La session est incorrecte.");
	    }

	    // Vérification de l'existence de l'élément et de l'étudiant dans la base de données
	    Element elementBD = iElement.findById(element.getId())
	            .orElseThrow(() -> new RuntimeException("L'élément avec l'ID " + element.getId() + " n'existe pas."));
	    Etudiant etudiantBD = iEtudiant.findById(etudiant.getId())
	            .orElseThrow(() -> new RuntimeException("L'étudiant avec l'ID " + etudiant.getId() + " n'existe pas."));

	    // Récupération des niveaux
	    Niveau niveauEtudiant = etudiantBD.getNiveau();
	    Niveau niveauModule = elementBD.getModule().getNiveau();

	    // Vérification de la cohérence des niveaux
	    if (!niveauEtudiant.equals(niveauModule) && !niveauEtudiant.getNiveauSuivant().equals(niveauModule)) {
	        throw new RuntimeException("Incohérence de niveau : l'étudiant et le module ne sont pas du même niveau.");
	    }

	    // Création et enregistrement du résultat
	    ResultatElement r = new ResultatElement();
	    r.setNote(note);
	    r.setElement(element);
	    r.setEtudiant(etudiant);
	    r.setSession(session);
	    iResultatElement.save(r);
	    logger.info("Association note : "+note+"element "+ element.getNom()+"a l'etudiant:"+ etudiant.getId()+"pour la session"+session);

	    return true;
	}
	@Override
	@Transactional
	public boolean modifierNoteElementEtudiant(Long resultatId, Double nouvelleNote, Element element, Etudiant etudiant) throws Exception {
	    // Vérification des nullités
	    if (nouvelleNote == null) {
	        throw new RuntimeException("Vous devez entrer la nouvelle note.");
	    }
	    
	    if (element == null) {
	        throw new RuntimeException("Vous devez entrer l'élément.");
	    }
	    if (etudiant == null) {
	        throw new RuntimeException("Vous devez entrer l'étudiant.");
	    }

	    // Vérification de la validité de la note
	    if (nouvelleNote < 0 || nouvelleNote > 20) {
	        throw new RuntimeException("La note doit être comprise entre 0 et 20.");
	    }
	   

	    // Vérification de l'existence de l'élément et de l'étudiant dans la base de données
	    Element elementBD = iElement.findById(element.getId())
	            .orElseThrow(() -> new RuntimeException("L'élément avec l'ID " + element.getId() + " n'existe pas."));
	    Etudiant etudiantBD = iEtudiant.findById(etudiant.getId())
	            .orElseThrow(() -> new RuntimeException("L'étudiant avec l'ID " + etudiant.getId() + " n'existe pas."));

	    // Récupération des niveaux
	    Niveau niveauEtudiant = etudiantBD.getNiveau();
	    Niveau niveauModule = elementBD.getModule().getNiveau();

	    // Vérification de la cohérence des niveaux
	    if (!niveauEtudiant.equals(niveauModule) && !niveauEtudiant.getNiveauSuivant().equals(niveauModule)) {
	        throw new RuntimeException("Incohérence de niveau : l'étudiant et le module ne sont pas du même niveau.");
	    }

	    // Récupération du résultat existant à modifier
	    ResultatElement resultat = iResultatElement.findById(resultatId)
	            .orElseThrow(() -> new RuntimeException("Le résultat avec l'ID " + resultatId + " n'existe pas."));

	    // Vérification que le résultat correspond à l'élément et à l'étudiant
	    if (!resultat.getElement().getId().equals(element.getId()) || !resultat.getEtudiant().getId().equals(etudiant.getId())) {
	        throw new RuntimeException("Le résultat ne correspond pas à l'élément ou à l'étudiant spécifié.");
	    }

	    // Modification de la note
	    resultat.setNote(nouvelleNote);
	    iResultatElement.save(resultat);
	    logger.info("Modification note : "+nouvelleNote+"element "+ element.getNom()+"a l'etudiant:"+ etudiant.getId()+"pour l'id resultat"+resultatId);


	    return true;
	}
	
	
	@Override
	public List<ResultatElement> getAllResultats() {
		return iResultatElement.findAll();
	}

}
