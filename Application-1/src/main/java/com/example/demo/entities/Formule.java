package com.example.demo.entities;

import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @ToString @Builder
public class Formule {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String nom;
	private String expression;
	
	@ElementCollection // Pour stocker une liste de paramètres
    @CollectionTable(name = "formule_parametres", joinColumns = @JoinColumn(name = "formule_id"))
    @Column(name = "parametre")
    private List<String> parametres; // Liste des paramètres (ex: A1, A10, etc.)
	
	@ManyToOne // Relation ManyToOne vers Niveau
    @JoinColumn(name = "niveau_id") // Clé étrangère vers Niveau
    private Niveau niveau; // Référence à Niveau
}
	

