package com.example.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Formule;
import com.example.demo.entities.Niveau;

import org.springframework.stereotype.Repository;

@Repository
public interface IFormule extends JpaRepository<Formule, Long> {
	List<Formule> findByNiveau(Niveau niveau);
	Optional<Formule> findByNiveauAndNom(Niveau niveau,String nom);

}
