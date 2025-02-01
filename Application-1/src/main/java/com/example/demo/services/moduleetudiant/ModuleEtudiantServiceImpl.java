package com.example.demo.services.moduleetudiant;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.repositories.IEtudiant;
import com.example.demo.repositories.IModule;
import com.example.demo.entities.Module;
import com.example.demo.entities.Niveau;
import com.example.demo.entities.Etudiant;

@Service
public class ModuleEtudiantServiceImpl implements IModuleEtudiantService{
	@Autowired
	private IEtudiant iEtudiant;
	@Autowired
	private IModule iModule;

	@Override
	@Transactional
	public boolean associerEtudiantModule(Etudiant etudiant, Module module) throws Exception {
	    // Vérification des paramètres null
	    if (module == null) {
	        throw new IllegalArgumentException("Le module ne peut pas être null.");
	    }
	    if (etudiant == null) {
	        throw new IllegalArgumentException("L'étudiant ne peut pas être null.");
	    }

	    // Vérification de l'existence de l'étudiant dans la base de données
	    Etudiant etudiantBD = iEtudiant.findById(etudiant.getId())
	        .orElseThrow(() -> new RuntimeException("L'étudiant avec l'ID " + etudiant.getId() + " n'existe pas."));

	    // Vérification de l'existence du module dans la base de données
	    Module moduleBD = iModule.findById(module.getId())
	        .orElseThrow(() -> new RuntimeException("Le module avec l'ID " + module.getId() + " n'existe pas."));

	    // Récupération des niveaux de l'étudiant et du module
	    Niveau niveauEtudiant = etudiantBD.getNiveau();
	    Niveau niveauModule = moduleBD.getNiveau();

	    // Vérification si l'étudiant est déjà inscrit à ce module
	    if (etudiantBD.getModules().contains(moduleBD)) {
	        throw new Exception("L'étudiant est déjà inscrit à ce module.");
	    }

	    // Vérification du nombre maximal de modules (8) pour l'étudiant
	    if (etudiantBD.getModules().size() >= 8) {
	        throw new Exception("L'étudiant est déjà inscrit à 8 modules.");
	    }

	    // Vérification de la cohérence des niveaux entre l'étudiant et le module
	    if (!niveauEtudiant.equals(niveauModule) && !niveauEtudiant.getNiveauSuivant().equals(niveauModule)) {
	        throw new Exception("Incohérence de niveau.");
	    }

	    // Ajouter le module à la liste des modules de l'étudiant
	    etudiantBD.getModules().add(moduleBD);
	    iEtudiant.save(etudiantBD);  // Sauvegarder l'étudiant avec le module ajouté

	    return true;
	}
	@Override @Transactional
    public boolean separerEtudiantModule(Etudiant etudiant, Module module) throws Exception {
	        // Vérification si l'étudiant existe
		if (module == null) {
	        throw new RuntimeException("Le module ne peut pas être null.");
	    }
	    if (etudiant == null) {
	        throw new RuntimeException("L'etudiant ne peut pas être null.");
	    }

	    // Vérification de l'existence de l'etudiant dans la base de données
	    if (iEtudiant.findById(etudiant.getId()).isEmpty()) {
	        throw new RuntimeException("L'etudiant avec l'ID " + etudiant.getId() + " n'existe pas.");
	    }

	    // Vérification de l'existence du module dans la base de données
	    if (iModule.findById(module.getId()).isEmpty()) {
	        throw new RuntimeException("Le module avec l'ID " + module.getId() + " n'existe pas.");
	    }

        // Vérification si l'étudiant est inscrit à ce module
        if (!etudiant.getModules().contains(module)) {
            throw new Exception("L'étudiant n'est pas inscrit à ce module.");
        }

        // Retirer le module de la liste des modules de l'étudiant
        etudiant.getModules().remove(module);
        iEtudiant.save(etudiant); // Sauvegarder l'étudiant avec le module retiré

        return true;
    }

	
}
