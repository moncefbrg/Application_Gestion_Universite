package com.example.demo.security.controller.utilisateur;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.security.entities.Utilisateur;
import com.example.demo.security.services.Utilisateur.IUtilisateurService;

import java.util.List;

@Controller
@RequestMapping("/utilisateurs")
public class UtilisateurController {

    @Autowired
    private IUtilisateurService utilisateurService;

    @GetMapping
    public String listeUtilisateurs(Model model) {
        List<Utilisateur> utilisateurs = utilisateurService.getAllUtilisateurs();
        model.addAttribute("utilisateurs", utilisateurs);
        return "utilisateurs";
    }

    @PostMapping
    public String ajouterUtilisateur(@RequestParam String username,
                                     @RequestParam String password,
                                     @RequestParam String role,
                                     RedirectAttributes redirectAttributes) {
        try {
            utilisateurService.creerUtilisateur(null, username, password, role);
            redirectAttributes.addFlashAttribute("success", "Utilisateur ajouté avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/utilisateurs";
    }

    @GetMapping("/{id}/edit")
    public String modifierUtilisateur(@PathVariable Long id, Model model) {
        Utilisateur utilisateur = utilisateurService.getUtilisateurById(id);
        model.addAttribute("utilisateur", utilisateur);
        return "utilisateurs";
    }

    @PostMapping("/{id}/update")
    public String mettreAJourUtilisateur(@PathVariable Long id,
                                         @RequestParam String username,
                                         @RequestParam String role,
                                         RedirectAttributes redirectAttributes) {
        try {
            utilisateurService.changerRoleUtilisateur(username, role);
            redirectAttributes.addFlashAttribute("success", "Utilisateur mis à jour");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/utilisateurs";
    }

    @GetMapping("/{id}/delete")
    public String supprimerUtilisateur(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        utilisateurService.supprimerUtilisateur(id);
        redirectAttributes.addFlashAttribute("success", "Utilisateur supprimé");
        return "redirect:/utilisateurs";
    }

    @GetMapping("/{id}/toggle-status")
    public String activerDesactiverUtilisateur(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        utilisateurService.toggleStatus(id);
        redirectAttributes.addFlashAttribute("success", "Statut de l'utilisateur mis à jour");
        return "redirect:/utilisateurs";
    }

    @GetMapping("/{id}/toggle-lock")
    public String verrouillerDeverrouillerUtilisateur(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        utilisateurService.toggleLock(id);
        redirectAttributes.addFlashAttribute("success", "Verrouillage/déverrouillage mis à jour");
        return "redirect:/utilisateurs";
    }
}

