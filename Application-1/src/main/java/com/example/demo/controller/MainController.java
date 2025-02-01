package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.services.resultatelement.IResultatElementService;

@Controller
public class MainController {
	
	@Autowired
	@Qualifier("ResultatElementServiceImpl")
	private IResultatElementService iResultatElementService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "Bienvenue au portail de l'université !");
        return "bienvenue";
    }

    @GetMapping("/login")
    public String login() {
        return "login";    //page de login
    }
    
    
    @GetMapping("/acceuil")
    public String acceuil() {
    	return "acceuil";
    }
    
    @GetMapping("/403")
    public String accessDenied() {
    	return "403";
    }
}


