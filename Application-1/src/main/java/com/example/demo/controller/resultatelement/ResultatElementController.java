package com.example.demo.controller.resultatelement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entities.Element;
import com.example.demo.entities.Etudiant;
import com.example.demo.entities.ResultatElement;
import com.example.demo.services.Etudiant.IEtudiantService;
import com.example.demo.services.element.IElementService;
import com.example.demo.services.resultatelement.IResultatElementService;

@Controller
@RequestMapping("/resultats")
public class ResultatElementController {

    @Autowired
    private IResultatElementService iResultatElementService;

    @Autowired
    private IEtudiantService iEtudiantService;

    @Autowired
    private IElementService iElementService;

    @GetMapping
    public String afficherResultats(Model model) {
        // Récupérer tous les résultats
        List<ResultatElement> resultats = iResultatElementService.getAllResultats();
        model.addAttribute("resultats", resultats);
        
        // Récupérer les étudiants et les éléments pour les afficher dans les formulaires
        List<Etudiant> etudiants = iEtudiantService.getAllEtudiants();
        model.addAttribute("etudiants", etudiants);

        List<Element> elements = iElementService.getAllElements();
        model.addAttribute("elements", elements);
        
        return "resultats";
    }

    @PostMapping("/ajouter")
    public String ajouterResultat(@RequestParam Double note,
                                  @RequestParam Long etudiantId,
                                  @RequestParam Long elementId,
                                  @RequestParam String session) {
        try {
            // Récupérer l'étudiant et l'élément par leurs IDs
            Etudiant etudiant = iEtudiantService.findById(etudiantId)
                    .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
            Element element = iElementService.findById(elementId)
                    .orElseThrow(() -> new RuntimeException("Élément non trouvé"));

            // Appeler le service pour associer la note, l'élément et l'étudiant
            iResultatElementService.associerNoteElementEtudiant(note, element, etudiant, session);

            return "redirect:/resultats"; // Rediriger vers la liste des résultats
        } catch (Exception e) {
            e.printStackTrace();
            return "error"; // Gérer les erreurs de manière appropriée
        }
    }

    @PostMapping("/modifier")
    public String modifierResultat(@RequestParam Long resultatId,
                                   @RequestParam Double nouvelleNote,
                                   @RequestParam Long etudiantId,
                                   @RequestParam Long elementId) {
        try {
            // Récupérer l'étudiant et l'élément par leurs IDs
            Etudiant etudiant = iEtudiantService.findById(etudiantId)
                    .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
            Element element = iElementService.findById(elementId)
                    .orElseThrow(() -> new RuntimeException("Élément non trouvé"));

            // Appeler le service pour modifier le résultat
            iResultatElementService.modifierNoteElementEtudiant(resultatId, nouvelleNote, element, etudiant);

            return "redirect:/resultats"; // Rediriger vers la liste des résultats
        } catch (Exception e) {
            e.printStackTrace();
            return "error"; // Gérer les erreurs de manière appropriée
        }
    }
}

