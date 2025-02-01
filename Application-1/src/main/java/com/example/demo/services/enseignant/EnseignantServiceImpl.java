package com.example.demo.services.enseignant;
//log done
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.Enseignant;
import com.example.demo.entities.Module;
import com.example.demo.repositories.IEnseignant;
import com.example.demo.repositories.IModule;

@Service
public class EnseignantServiceImpl implements IEnseignantService {
	@Autowired
	IEnseignant iEnseignant;
	@Autowired
	IModule iModule;
    private static final Logger logger = LoggerFactory.getLogger(EnseignantServiceImpl.class);
	@Override
	@Transactional
	public boolean creeEnseignant(Long id, String nom, String prenom, String cni, List<Module> modules) {
	    // Vérifier que les entrées ne sont pas null ou invalides
	    if (id == null) {
	        throw new IllegalArgumentException("L'ID de l'enseignant ne peut pas être null.");
	    }
	    if (nom == null || nom.trim().isEmpty()) {
	        throw new IllegalArgumentException("Le nom de l'enseignant ne peut pas être null ou vide.");
	    }
	    if (prenom == null || prenom.trim().isEmpty()) {
	        throw new IllegalArgumentException("Le prénom de l'enseignant ne peut pas être null ou vide.");
	    }
	    if (cni == null || cni.trim().isEmpty()) {
	        throw new IllegalArgumentException("Le CNI de l'enseignant ne peut pas être null ou vide.");
	    }
	    if (modules == null || modules.isEmpty()) {
	        throw new IllegalArgumentException("La liste des modules ne peut pas être null ou vide.");
	    }

	    // Vérifier si un enseignant avec le même ID existe déjà
	    if (iEnseignant.findById(id).isPresent()) {
	        throw new RuntimeException("Un enseignant avec l'ID " + id + " existe déjà.");
	    }

	    // Vérifier si un enseignant avec le même CNI existe déjà
	    if (iEnseignant.findByCni(cni).isPresent()) {
	        throw new RuntimeException("Un enseignant avec le CNI " + cni + " existe déjà.");
	    }

	    // Vérifier si tous les modules existent
	    List<String> modulesManquants = modules.stream()
	    		.map(Module->Module.getNom())
	            .filter(moduleNom -> iModule.findByNom(moduleNom).isEmpty())
	            .collect(Collectors.toList());
	            

	    if (!modulesManquants.isEmpty()) {
	        throw new RuntimeException("Les modules suivants n'existent pas : " + modulesManquants);
	    }

	    // Créer l'enseignant
	    Enseignant enseignant = Enseignant.builder()
	            .id(id)
	            .nom(nom)
	            .prenom(prenom)
	            .cni(cni)
	            .modules(modules)
	            .build();

	    // Sauvegarder l'enseignant
	    iEnseignant.save(enseignant);
	    logger.info("creation de l'enseignant"+id+","+nom+","+prenom+","+cni);

	    return true; // Retourne true si la création est réussie
	}

	@Override
	@Transactional
	public boolean supprimerEnseignant(Long id) {
	    // Vérifier que l'ID n'est pas null
	    if (id == null) {
	        throw new IllegalArgumentException("L'ID de l'enseignant ne peut pas être null.");
	    }

	    // Rechercher l'enseignant par son ID
	    Optional<Enseignant> enseignantOptional = iEnseignant.findById(id);
	    if (enseignantOptional.isEmpty()) {
	        throw new RuntimeException("L'enseignant avec l'ID " + id + " n'existe pas.");
	    }

	    // Récupérer l'enseignant à supprimer
	    Enseignant enseignant = enseignantOptional.get();
	    logger.info("supression de l'enseignant"+id);


	    // Supprimer l'enseignant
	    iEnseignant.delete(enseignant);

	    return true; // Retourne true si la suppression est réussie
	}
	@Override
	@Transactional
	public boolean modifierEnseignant(Long id, String nom, String prenom, String cni, List<Module> modules) {
	    // Vérifier que les entrées ne sont pas null ou invalides
	    if (id == null) {
	        throw new IllegalArgumentException("L'ID de l'enseignant ne peut pas être null.");
	    }
	    if (nom == null || nom.trim().isEmpty()) {
	        throw new IllegalArgumentException("Le nom de l'enseignant ne peut pas être null ou vide.");
	    }
	    if (prenom == null || prenom.trim().isEmpty()) {
	        throw new IllegalArgumentException("Le prénom de l'enseignant ne peut pas être null ou vide.");
	    }
	    if (cni == null || cni.trim().isEmpty()) {
	        throw new IllegalArgumentException("Le CNI de l'enseignant ne peut pas être null ou vide.");
	    }
	    if (modules == null || modules.isEmpty()) {
	        throw new IllegalArgumentException("La liste des modules ne peut pas être null ou vide.");
	    }

	    // Rechercher l'enseignant par son ID
	    Optional<Enseignant> enseignantOptional = iEnseignant.findById(id);
	    if (enseignantOptional.isEmpty()) {
	        throw new RuntimeException("L'enseignant avec l'ID " + id + " n'existe pas.");
	    }

	    // Récupérer l'enseignant à modifier
	    Enseignant enseignant = enseignantOptional.get();

	    // Vérifier si un autre enseignant avec le même CNI existe déjà
	    Optional<Enseignant> enseignantAvecMemeCni = iEnseignant.findByCni(cni);
	    if (enseignantAvecMemeCni.isPresent() && !enseignantAvecMemeCni.get().getId().equals(id)) {
	        throw new RuntimeException("Un enseignant avec le CNI " + cni + " existe déjà.");
	    }

	    // Vérifier si tous les modules existent
	    List<String> modulesManquants = modules.stream()
	            .map(Module::getNom)
	            .filter(moduleNom -> iModule.findByNom(moduleNom).isEmpty())
	            .collect(Collectors.toList());

	    if (!modulesManquants.isEmpty()) {
	        throw new RuntimeException("Les modules suivants n'existent pas : " + modulesManquants);
	    }

	    // Mettre à jour les attributs de l'enseignant
	    enseignant.setNom(nom);
	    enseignant.setPrenom(prenom);
	    enseignant.setCni(cni);
	    enseignant.setModules(modules);

	    // Sauvegarder l'enseignant (automatiquement géré par @Transactional)
	    iEnseignant.save(enseignant);
	    logger.info("modification de l'enseignant"+id+"avec :"+nom+","+prenom+","+cni);


	    return true; // Retourne true si la modification est réussie
	}

	@Override
	public List<Enseignant> getAllEnseignants() {
		return iEnseignant.findAll();
	}

	@Override
	public Optional<Enseignant> getEnseignantById(Long id) {
		return iEnseignant.findById(id);
	}

	@Override
	public List<Enseignant> findAll() {
		return iEnseignant.findAll();
	}

	@Override
	public Optional<Enseignant> findById(Long id) {
		return iEnseignant.findById(id);
	}
	

}
