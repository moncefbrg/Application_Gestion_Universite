package com.example.demo.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity @Data @AllArgsConstructor @NoArgsConstructor @Builder
public class Niveau {
	@Id
    private Long id;

    private String nom;
    private String alias;

    
    @OneToOne
    @JoinColumn(name = "niveau_suivant_id")
    private Niveau niveauSuivant;
   
    @OneToMany (mappedBy= "niveau")
    private List <Etudiant> etudiants;
    
    @OneToMany (mappedBy= "niveau")
    private List<Module> modules; 
    
    @OneToMany(mappedBy="niveau")
    private List<Formule> formules;
    
    @OneToOne(mappedBy="niveau")
    private Seuil seuil;
    
}
