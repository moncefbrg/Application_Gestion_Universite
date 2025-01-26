package com.example.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Classe;
import com.example.demo.entities.Etudiant;

@Repository
public interface IClasse extends JpaRepository<Classe, Long>{

	Optional<Classe> findByNom(String nomClasse);
	
	// Méthode pour retourner la liste des étudiants d'une classe donnée
    @Query("SELECT e FROM Etudiant e WHERE e.classe.nom = :nomClasse")
    List<Etudiant> findEtudiantsByNomClasse(@Param("nomClasse") String nomClasse);
}

