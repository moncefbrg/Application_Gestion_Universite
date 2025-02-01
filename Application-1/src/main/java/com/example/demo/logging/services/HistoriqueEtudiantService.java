package com.example.demo.logging.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.logging.entities.HistoriqueEtudiant;
import com.example.demo.logging.repositories.HistoriqueEtudiantRepository;

@Service
public class HistoriqueEtudiantService {
	@Autowired
	private HistoriqueEtudiantRepository historiqueEtudiantRepository;
	
	public void saveHistoriqueEtudiant(String oldValues,String newValues) {
		HistoriqueEtudiant hE = new HistoriqueEtudiant();
		hE.setLogDate(LocalDateTime.now());
		hE.setOldValues(oldValues);
		hE.setNewValues(newValues);
		historiqueEtudiantRepository.save(hE);
    }

}
