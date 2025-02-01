package com.example.demo.controller.enseignant;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entities.Enseignant;
import com.example.demo.services.enseignant.IEnseignantService;
import com.example.demo.entities.Module;

@Controller
@RequestMapping("/enseignants")
public class EnseignantController {

    @Autowired
    private IEnseignantService enseignantService;

    // Afficher la liste des enseignants
    @GetMapping
    public String afficherEnseignants(Model model) {
        List<Enseignant> enseignants = enseignantService.findAll(); // Tu dois avoir une méthode findAll dans le service
        model.addAttribute("enseignants", enseignants);
        return "enseignants"; // Retourne la vue enseignant.html
    }

    // Ajouter un enseignant
    @PostMapping
    public String ajouterEnseignant(@RequestParam Long id, @RequestParam String nom, @RequestParam String prenom,
                                    @RequestParam String cni, @RequestParam List<Module> modules) {
        try {
            boolean created = enseignantService.creeEnseignant(id, nom, prenom, cni, modules);
            if (created) {
                return "redirect:/enseignants"; // Rediriger vers la liste des enseignants après ajout
            } else {
                return "error"; // Gérer l'erreur
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error"; // Gérer l'erreur
        }
    }

    // Modifier un enseignant
    @GetMapping("/{id}/edit")
    public String modifierEnseignantForm(@PathVariable Long id, Model model) {
        Optional<Enseignant> enseignantOptional = enseignantService.findById(id); // Il faut une méthode findById dans ton service
        if (enseignantOptional.isPresent()) {
            model.addAttribute("enseignant", enseignantOptional.get());
            return "modifierEnseignant"; // Vue pour le formulaire de modification
        } else {
            return "error"; // Gérer l'erreur (enseignant non trouvé)
        }
    }

    @PostMapping("/{id}/edit")
    public String modifierEnseignant(@PathVariable Long id, @RequestParam String nom, @RequestParam String prenom,
                                     @RequestParam String cni, @RequestParam List<Module> modules) {
        try {
            boolean updated = enseignantService.modifierEnseignant(id, nom, prenom, cni, modules);
            if (updated) {
                return "redirect:/enseignants"; // Rediriger vers la liste des enseignants après modification
            } else {
                return "error"; // Gérer l'erreur
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error"; // Gérer l'erreur
        }
    }

    // Supprimer un enseignant
    @GetMapping("/{id}/delete")
    public String supprimerEnseignant(@PathVariable Long id) {
        try {
            boolean deleted = enseignantService.supprimerEnseignant(id);
            if (deleted) {
                return "redirect:/enseignants"; // Rediriger vers la liste après suppression
            } else {
                return "error"; // Gérer l'erreur
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error"; // Gérer l'erreur
        }
    }
}
