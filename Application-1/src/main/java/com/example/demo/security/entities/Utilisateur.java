package com.example.demo.security.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @ToString
public class Utilisateur {
	@Id
	private Long id;
	
	@Column(unique=true, nullable=false)
	private String username;
	
	@Column(nullable=false)
	private String password;
	
	private boolean enabled = true; //par défaut, activé
	private boolean locked = false;
	
	@ManyToOne
	@JoinColumn(name="personne_id", nullable=false)
	private Personne personne;  //chaque compte est lié à une seule personne
	
	@ManyToOne
	@JoinColumn(name="role_id", nullable=false)
	private Role role;    //chaque compte a un seul role 
		
}
