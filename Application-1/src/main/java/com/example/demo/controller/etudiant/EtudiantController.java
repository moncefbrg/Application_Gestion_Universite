package com.example.demo.controller.etudiant;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entities.Classe;
import com.example.demo.entities.Etudiant;
import com.example.demo.entities.Niveau;
import com.example.demo.services.Etudiant.IEtudiantService;
import com.example.demo.services.classe.IClasseService;
import com.example.demo.services.niveau.INiveauService;

@Controller
@RequestMapping("/etudiants")
public class EtudiantController {
	
	@Autowired
	private IEtudiantService iEtudiantService;
	
	@Autowired
	private INiveauService iNiveauService;
	
	@Autowired
	private IClasseService iClasseService;
	
	//Affichage de la liste des etudiants
	@GetMapping
	public String afficherEtudiants(Model model) {
		List <Etudiant> etudiants=iEtudiantService.getAllEtudiants();
        model.addAttribute("etudiants",etudiants);
        model.addAttribute("niveaux", iNiveauService.getAllNiveaux());
        model.addAttribute("classes", iClasseService.getAllClasses());
        return "etudiants";
    }
	
	//Ajouter un etudiant
	@PostMapping
	public String ajouterEtudiant(@RequestParam Long id,
			@RequestParam String nom,
			@RequestParam String prenom,
			@RequestParam String cne,
			@RequestParam String aliasNiveau, 
			@RequestParam String nomClasse) throws Exception {
	
	boolean added= iEtudiantService.ajouterEtudiant(id, nom, prenom, cne, aliasNiveau, nomClasse);
	if (added) {
		return "redirect:/etudiants";
	} else {
		return "error";
	}
  }
	
	// Formulaire pour modifier un étudiant
    @GetMapping("/{id}/edit")
    public String afficherFormulaireModificationEtudiant(@PathVariable Long id, Model model) throws Exception {
        Etudiant etudiant = iEtudiantService.chercherEtudiantById(id);
        List<Niveau> niveaux = iNiveauService.getAllNiveaux();
        List<Classe> classes = iClasseService.getAllClasses();
        model.addAttribute("etudiant", etudiant);
        model.addAttribute("niveaux", niveaux);
        model.addAttribute("classes", classes);
        return "modifier_etudiant"; // Page pour modifier l'étudiant
    }
	
	
    //traitement du formulaire
	@PostMapping("/{id}/edit")
	public String modifierEtudiant(@PathVariable Long id,
			@RequestParam String nom, 
			@RequestParam String prenom, 
			@RequestParam String cne,
			@RequestParam Long niveau) throws Exception {
		
		boolean updated = iEtudiantService.modifierEtudiant(id, nom, prenom, cne, niveau);
        if (updated) {
            return "redirect:/etudiants"; // Rediriger vers la liste des étudiants après modification
        } else {
            return "error"; // Page d'erreur si la modification échoue
        }
    }
	
	//supprimer un etudiant
	@GetMapping("/{id}/delete")
    public String supprimerEtudiant(@PathVariable Long id) throws Exception {
        boolean deleted = iEtudiantService.supprimerEtudiant(id);
        if (deleted) {
            return "redirect:/etudiants"; // Rediriger vers la liste des étudiants après suppression
        } else {
            return "error"; // Page d'erreur si la suppression échoue
        }
    }
	
		
	}



