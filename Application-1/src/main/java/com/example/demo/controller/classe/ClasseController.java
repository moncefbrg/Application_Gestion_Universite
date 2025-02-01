package com.example.demo.controller.classe;

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
import com.example.demo.entities.Niveau;
import com.example.demo.services.classe.IClasseService;
import com.example.demo.services.niveau.INiveauService;

@Controller
@RequestMapping("/classes")
public class ClasseController {

    @Autowired
    private IClasseService classeService;

    @Autowired
    private INiveauService niveauService;

    // 1. Afficher la liste des classes
    @GetMapping
    public String afficherClasses(Model model) {
        List<Classe> classes = classeService.getAllClasses();
        model.addAttribute("classes", classes);
        List<Niveau> niveaux = niveauService.getAllNiveaux();
        model.addAttribute("niveaux", niveaux);
        return "classes"; // Thymeleaf va chercher classes.html
    }

    // 2. Ajouter une classe
    @PostMapping
    public String ajouterClasse(@RequestParam String nom, @RequestParam Long niveauId) {
        try {
            // Récupérer le niveau à partir de son ID
            Niveau niveau = niveauService.getNiveauById(niveauId);

            // Appeler la méthode creerClasse avec le nom
            classeService.creerClasse(nom);  // La méthode crée la classe en utilisant seulement le nom
            // Récupérer la classe nouvellement créée
            Classe classe = classeService.getClasseByNom(nom);  // Utilisation de la méthode nouvellement ajoutée
            classe.setNiveau(niveau); // Associer le niveau
            classeService.save(classe); // Sauvegarder l'association niveau

        } catch (RuntimeException e) {
            // Si la classe existe déjà, on redirige avec un message d'erreur
            return "redirect:/classes?error=" + e.getMessage();
        }
        return "redirect:/classes";  // Redirection après ajout
    }

    // 3. Modifier une classe
    @PostMapping("/modifier/{id}")
    public String modifierClasse(@PathVariable Long id, @RequestParam String nouveauNom) {
        try {
            // Récupérer la classe par son ID pour avoir l'ancien nom
            Classe classe = classeService.getClasseByNom(id.toString());  // Utilisation du nom de la classe
            classeService.modifierClasse(classe.getNom(), nouveauNom);  // Appeler modifierClasse avec l'ancien nom et le nouveau nom
        } catch (RuntimeException e) {
            return "redirect:/classes?error=" + e.getMessage();
        }
        return "redirect:/classes"; // Redirection après modification
    }

    // 4. Supprimer une classe
    @PostMapping("/supprimer/{id}")
    public String supprimerClasse(@PathVariable Long id) {
        try {
            // Récupérer la classe par son ID pour obtenir le nom
            Classe classe = classeService.getClasseByNom(id.toString());  // Utilisation du nom de la classe
            classeService.supprimerClasse(classe.getNom());  // Appeler supprimerClasse avec le nom
        } catch (RuntimeException e) {
            return "redirect:/classes?error=" + e.getMessage();
        }
        return "redirect:/classes";  // Redirection après suppression
    }
}


