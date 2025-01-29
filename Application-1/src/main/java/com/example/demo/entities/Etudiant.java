package com.example.demo.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity @Data @AllArgsConstructor @NoArgsConstructor @Builder
public class Etudiant {
	@Id
	private Long id;
	@Column(unique=true)
    private String cne;
    private String nom;
    private String prenom;
    @ManyToOne
    @JsonBackReference  // Ignorer la sérialisation de la relation Niveau -> Etudiant

    private Niveau niveau;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "etudiant_module",
        joinColumns = @JoinColumn(name = "etudiant_id"),
        inverseJoinColumns = @JoinColumn(name = "module_id")
    )
    
    private List<Module> modules;
    
    @ManyToOne
    @JoinColumn(name = "classe_id") // Clé étrangère vers Classe
    private Classe classe;
    
}
