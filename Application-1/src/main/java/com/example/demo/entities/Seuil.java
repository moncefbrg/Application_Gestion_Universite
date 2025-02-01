package com.example.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seuil {
    @Id
    private Long id;
    
    @Column(name="xnormale")
    private double XNormale;// le seuil X de la session normale
    
    @Column(name="yrattrapage")
    private double YRattrapage; // le seuil Y de la session rattrapage

    // Ajoute la relation avec Niveau
    @ManyToOne
    private Niveau niveau;  // Relation vers Niveau
}

