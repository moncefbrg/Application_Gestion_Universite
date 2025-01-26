package com.example.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Seuil {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
    private double XNormale;   //le seuil X de la session normale
    private double YRattrapage; // le seuil Y de la session rattrapage
    
    @OneToOne
    @JoinColumn(name="niveau_id")
    private Niveau niveau;
    

}
