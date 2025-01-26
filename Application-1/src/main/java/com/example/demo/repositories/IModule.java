package com.example.demo.repositories;

import java.util.List;
import java.util.Optional;

import com.example.demo.entities.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IModule extends JpaRepository<Module, Long>{
	List<Module> findByNiveauId(Long niveauId); //retourne les modules d'un niveau
	Optional<Module> findByNom(String nom);

}
