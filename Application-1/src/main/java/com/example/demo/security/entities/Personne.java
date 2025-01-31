package com.example.demo.security.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @ToString
public class Personne {
	@Id
	private Long id;
	
	@Column(nullable=false)
	private String nom;
	
	@Column(nullable=false)
	private String prenom;
	
	@Column(unique=true, nullable=false)
	private String cin;
	
	private String email;
	
	private String telephone;
	
	@OneToMany(mappedBy="personne", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private List<Utilisateur> comptes = new ArrayList<>();

}
