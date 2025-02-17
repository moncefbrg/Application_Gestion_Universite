package com.example.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Niveau;

@Repository
public interface INiveau extends JpaRepository<Niveau, Long>{
	Optional<Niveau> findByNom(String nom);
	Optional<Niveau> findByAlias(String alias);
	List<Niveau> findByNiveauSuivant(Niveau niveau);
	

}
