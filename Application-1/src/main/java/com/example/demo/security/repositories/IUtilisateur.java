package com.example.demo.security.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.security.entities.Utilisateur;

public interface IUtilisateur extends JpaRepository<Utilisateur, Long>{
	Optional<Utilisateur> findByUsername(String username);

	boolean existsByUsername(String username);
}
