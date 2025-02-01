package com.example.demo.controller.niveau;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entities.Formule;
import com.example.demo.entities.Niveau;
import com.example.demo.entities.Seuil;
import com.example.demo.entities.Module;
import com.example.demo.services.formule.IFormuleService;
import com.example.demo.services.module.IModuleService;
import com.example.demo.services.niveau.INiveauService;
import com.example.demo.services.seuil.ISeuilService;

import java.util.List;

@Controller
@RequestMapping("/niveaux")
public class NiveauController {

    @Autowired
    private INiveauService iNiveauService;

    @Autowired
    private ISeuilService iSeuilService;

    // Afficher la liste des niveaux
    @GetMapping
    public String listeNiveaux(Model model) {
    	List<Niveau> niveaux = iNiveauService.getAllNiveaux();
        model.addAttribute("niveaux", niveaux);
        model.addAttribute("seuils", iSeuilService.getAllSeuils());
        return "niveaux"; // Nom de la vue Thymeleaf (niveaux.html)
    }

    @PostMapping
    public String ajouterNiveau(@ModelAttribute Niveau niveau) throws Exception {
        iNiveauService.creerNiveau(niveau.getId(), niveau.getNom(), niveau.getAlias(), niveau.getNiveauSuivant(), niveau.getSeuil());
        return "redirect:/niveaux";
    }
    
    @GetMapping("/modifier/{id}")
    public String afficherModification(@PathVariable Long id, Model model) {
        Niveau niveau = iNiveauService.getNiveauById(id); // Récupère le niveau par son ID
        model.addAttribute("niveau", niveau); // Passe le niveau au modèle pour la vue
        model.addAttribute("niveaux", iNiveauService.getAllNiveaux());
        model.addAttribute("seuils", iSeuilService.getAllSeuils());
        return "niveaux"; // La même vue que pour la liste
    }

    
    @PostMapping("/modifier/{id}")
    public String modifierNiveau(@PathVariable Long id, 
    		@RequestParam String nom,
    		@RequestParam String alias,
    		@RequestParam Long niveauSuivantId,
    		@RequestParam Long seuilId) throws Exception {
    	
    	Niveau niveauSuivant = (niveauSuivantId != null) ? iNiveauService.getNiveauById(niveauSuivantId) : null;
    	Seuil seuil = (seuilId != null) ? iSeuilService.getSeuilById(seuilId) : null;
    	iNiveauService.modifierNiveau(id, nom, alias, niveauSuivant, null, seuil, null);
    	return "redirect:/niveaux";
    }
    
    @PostMapping("/supprimer/{id}")
    public String supprimerNiveau(@RequestParam String nom) {
    	iNiveauService.supprimerNiveau(nom);
    	return "redirect:/niveaux";
    }
    
    


}