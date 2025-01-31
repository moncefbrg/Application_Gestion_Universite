package com.example.demo.security.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.security.entities.Personne;

public interface IPersonne extends JpaRepository<Personne, Long>{
	Optional<Personne> findByCin(String cin);
}
