package com.example.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
    private Niveau niveau;
}
