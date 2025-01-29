package com.example.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Element;
import com.example.demo.entities.Etudiant;
import com.example.demo.entities.ResultatElement;

@Repository
public interface IResultatElement extends JpaRepository<ResultatElement, Long> {
	List<ResultatElement> findBySession(String session);
	Optional<ResultatElement> findByElementAndSessionAndEtudiant(Element element,String session,Etudiant etudiant);
}
