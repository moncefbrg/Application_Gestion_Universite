package com.example.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ResultatElement {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private double note;
	private String session;
	
	@ManyToOne
	@JoinColumn(name="etudiant_id")
	private Etudiant etudiant;
	
	@ManyToOne
	@JoinColumn(name="element_id")
	private Element element;
	
	

}
