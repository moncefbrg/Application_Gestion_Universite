package com.example.demo.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.entities.Etudiant;

@Repository
public interface IEtudiant extends JpaRepository<Etudiant, Long>{
	public Optional<Etudiant> findById(Long id);
	List<Etudiant> findByCneOrNomOrPrenomOrNiveauId(String cne, String nom, String prenom, Long niveauId);
	
}
