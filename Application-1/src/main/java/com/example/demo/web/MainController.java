package com.example.demo.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class MainController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "Bienvenue au portail de l'université !");
        return "bienvenue";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    
    @GetMapping("/acceuil")
    public String acceuil() {
    	return "acceuil";
    }
    

    @GetMapping("/etudiants")
    public String etudiants(Model model) {
        return "etudiants";
    }


    @GetMapping("/niveaux")
    public String niveaux(Model model) {
        return "niveaux";
    }


    @GetMapping("/modules")
    public String modules(Model model) {
        return "modules";
    }


    @GetMapping("/enseignants")
    public String enseignants(Model model) {
        return "enseignants";
    }


    @GetMapping("/resultats")
    public String resultats(Model model) {
        return "resultats";
    }
    

    @GetMapping("/classes")
    public String classes(Model model) {
    	return "classes";
    }
}


