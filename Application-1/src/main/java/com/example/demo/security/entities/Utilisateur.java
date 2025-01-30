package com.example.demo.security.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
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
	
	@ElementCollection(fetch=FetchType.EAGER)
	private Set<String> roles = new HashSet<>();
	
	public void addRole(String role) {
		this.roles.add(role);
	}
	
	public void removeRole(String role) {
		this.roles.remove(role);
	}
		
}
