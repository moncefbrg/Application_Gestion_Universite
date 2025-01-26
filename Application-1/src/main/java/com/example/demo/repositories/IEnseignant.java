package com.example.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Enseignant;

public interface IEnseignant extends JpaRepository<Enseignant, Long>{
	Optional<Enseignant> findByCni(String cni);

}
