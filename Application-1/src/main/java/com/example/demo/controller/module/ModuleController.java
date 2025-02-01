package com.example.demo.controller.module;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.repositories.IEnseignant;
import com.example.demo.repositories.INiveau;
import com.example.demo.services.element.IElementService;
import com.example.demo.services.enseignant.IEnseignantService;
import com.example.demo.services.module.IModuleService;
import com.example.demo.services.niveau.INiveauService;
import com.example.demo.entities.Element;
import com.example.demo.entities.Enseignant;
import com.example.demo.entities.Module;
import com.example.demo.entities.Niveau;

@Controller
@RequestMapping("/modules")
public class ModuleController {

    @Autowired
    private IModuleService moduleService;
    
    @Autowired
    private INiveauService niveauService;
    
    @Autowired
    private IEnseignantService enseignantService;
    
    @Autowired
    private IElementService elementService;

    // 1. Afficher la liste des modules
    @GetMapping
    public String afficherModules(Model model) {
        List<Module> modules = moduleService.getAllModules();
        if (modules == null || modules.isEmpty()) {
            model.addAttribute("modules", new ArrayList<>()); // Éviter null
        } else {
            //pour chaque module, on ajoute les elements associés
            for(Module module : modules) {
            	List<Element> elements=elementService.getElementsByModule(module);
            	module.setElements(elements);
            }
            model.addAttribute("modules", modules);
        }
        List<Niveau> niveaux = niveauService.getAllNiveaux();
        List<Enseignant> enseignants = enseignantService.getAllEnseignants();
        model.addAttribute("niveaux", niveaux);
        model.addAttribute("enseignants", enseignants);

        return "modules"; // Thymeleaf va chercher modules.html
    }

    // 2. Ajouter un module
    @PostMapping
    public String ajouterModule(@RequestParam String nom, 
                                @RequestParam String semestre, 
                                @RequestParam Long niveau, 
                                @RequestParam(required = false) Long enseignant) {
        
        Niveau niveauObj = niveauService.getNiveauById(niveau);
        Enseignant enseignantObj = enseignant != null ? 
        	    enseignantService.getEnseignantById(enseignant).orElse(null) : null;

        moduleService.creerModule(null, nom, semestre, niveauObj, enseignantObj);
        
        return "redirect:/modules"; // Redirige après ajout
    }

    // 3. Modifier un module
    @PostMapping("/modifier/{id}")
    public String modifierModule(@PathVariable Long id, 
                                 @RequestParam String nom, 
                                 @RequestParam String semestre) {
        moduleService.modifierModule(id, nom, semestre, null);
        return "redirect:/modules";
    }

    // 4. Supprimer un module
    @PostMapping("/supprimer/{id}")
    public String supprimerModule(@PathVariable Long id) {
        moduleService.supprimerModule(id);
        return "redirect:/modules";
    }
}


