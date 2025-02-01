package com.example.demo.services.module;
//log done
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.Element;
import com.example.demo.entities.Enseignant;
import com.example.demo.entities.Module;
import com.example.demo.entities.Niveau;
import com.example.demo.repositories.IElement;
import com.example.demo.repositories.IEnseignant;
import com.example.demo.repositories.IModule;
import com.example.demo.repositories.INiveau;

@Service
public class ModuleServiceImpl implements IModuleService{
	@Autowired
	private IModule iModule;
	@Autowired
	private INiveau iNiveau;
	@Autowired
	private IElement iElement;
	@Autowired
	private IEnseignant iEnseignant;
	
	private static final Logger logger=LoggerFactory.getLogger(ModuleServiceImpl.class);

	@Override
	@Transactional
	public Module creerModule(Long id, String nom, String semestre,Niveau niveau,Enseignant enseignant) {
	    // Vérifier que les entrées ne sont pas null ou invalides
	    if (id == null) {
	        throw new IllegalArgumentException("L'ID du module ne peut pas être null.");
	    }
	    if (nom == null || nom.trim().isEmpty()) {
	        throw new IllegalArgumentException("Le nom du module ne peut pas être null ou vide.");
	    }
	    if (semestre == null || semestre.trim().isEmpty()) {
	        throw new IllegalArgumentException("Le semestre ne peut pas être null ou vide.");
	    }
	    

	    // Vérifier si un module avec le même ID existe déjà
	    if (iModule.findById(id).isPresent()) {
	        throw new RuntimeException("Un module avec l'ID " + id + " existe déjà.");
	    }

	    // Vérifier si un module avec le même nom existe déjà
	    if (iModule.findByNom(nom).isPresent()) {
	        throw new RuntimeException("Un module avec le nom " + nom + " existe déjà.");
	    }
	 // Vérifier si un module avec le même nom existe déjà
	    if (iNiveau.findById(niveau.getId()).isEmpty()) {
	        throw new RuntimeException("Le niveau avec le nom " + niveau.getNom() + " existe pas.");
	    }
	 // Vérifier si un module avec le même nom existe déjà
	    if (!(enseignant==null) && iEnseignant.findById(enseignant.getId()).isEmpty()) {
	        throw new RuntimeException("L'enseignant avec le nom " + enseignant.getNom() + " existe pas.");
	    }


	    // Créer le module
	    Module module = Module.builder()
	            .id(id)
	            .nom(nom)
	            .semestre(semestre)
	            .enseignant(enseignant==null?null:enseignant)
	            .niveau(niveau)
	            .build();

	    // Sauvegarder le module
	    iModule.save(module);
	    logger.info("Creation de module avec :"+id+","+nom+","+semestre+","+niveau.getAlias()+"enseignant :"+enseignant.getId());

	    return module; // Retourne true si la création est réussie
	}

	@Override
	@Transactional
	public boolean modifierModule(Long id, String nom, String semestre, List<Element> elements) {
	    // Vérifier que l'ID n'est pas null
	    if (id == null) {
	        throw new IllegalArgumentException("L'ID du module ne peut pas être null.");
	    }

	    // Rechercher le module par son ID
	    Optional<Module> moduleOptional = iModule.findById(id);
	    if (moduleOptional.isEmpty()) {
	        throw new RuntimeException("Le module avec l'ID " + id + " n'existe pas.");
	    }

	    // Récupérer le module à modifier
	    Module module = moduleOptional.get();

	    // Mettre à jour les attributs du module (uniquement si les valeurs ne sont pas null)
	    if (nom != null && !nom.trim().isEmpty()) {
	        // Vérifier si un module avec le même nom existe déjà (autre que celui en cours de modification)
	        Optional<Module> moduleAvecMemeNom = iModule.findByNom(nom);
	        if (moduleAvecMemeNom.isPresent() && !moduleAvecMemeNom.get().getId().equals(id)) {
	            throw new RuntimeException("Un module avec le nom " + nom + " existe déjà.");
	        }
	        module.setNom(nom);
	    }

	    if (semestre != null && !semestre.trim().isEmpty()) {
	        module.setSemestre(semestre);
	    }

	    if (elements != null && !elements.isEmpty()) { 
	        module.setElements(elements);
	    }

	    // Sauvegarder le module (automatiquement géré par @Transactional)
	    iModule.save(module);
	    logger.info("Modification du module :"+id);

	    return true; // Retourne true si la modification est réussie
	}
	@Override
	@Transactional
	public boolean supprimerModule(Long id) {
	    // Vérifier que l'ID n'est pas null
	    if (id == null) {
	        throw new IllegalArgumentException("L'ID du module ne peut pas être null.");
	    }

	    // Rechercher le module par son ID
	    Optional<Module> moduleOptional = iModule.findById(id);
	    if (moduleOptional.isEmpty()) {
	        throw new RuntimeException("Le module avec l'ID " + id + " n'existe pas.");
	    }

	    // Récupérer le module à supprimer
	    Module module = moduleOptional.get();

	    // Supprimer les éléments associés au module
	    List<Element> elementsASupprimer = module.getElements();
	    if (elementsASupprimer != null && !elementsASupprimer.isEmpty()) {
	        for (Element element : elementsASupprimer) {
	            iElement.delete(element);
	        }
	    }

	    // Supprimer le module
	    iModule.delete(module);
	    logger.info("Supression du module :"+id);

	    return true; // Retourne true si la suppression est réussie
	}

	@Override
	@Transactional
	public void associerModuleEnseignant(Module module, Enseignant enseignant) throws Exception{
	    // Vérification des nullités
	    if (module == null) {
	        throw new RuntimeException("Le module ne peut pas être null.");
	    }
	    if (enseignant == null) {
	        throw new RuntimeException("L'enseignant ne peut pas être null.");
	    }

	    // Vérification de l'existence de l'enseignant dans la base de données
	    if (iEnseignant.findById(enseignant.getId()).isEmpty()) {
	        throw new RuntimeException("L'enseignant avec l'ID " + enseignant.getId() + " n'existe pas.");
	    }

	    // Vérification de l'existence du module dans la base de données
	    if (iModule.findById(module.getId()).isEmpty()) {
	        throw new RuntimeException("Le module avec l'ID " + module.getId() + " n'existe pas.");
	    }
	    if (!(iModule.findById(module.getId()).get().getEnseignant()==null)) {
	        throw new RuntimeException("Le module avec l'ID " + module.getId() + " est deja associer a un autre enseignant.");
	    }

	    // Association de l'enseignant au module
	    module.setEnseignant(enseignant);
	    iModule.save(module);
	    logger.info("Association d'enseignant"+enseignant.getId()+" au module :"+module.getNom());

	}
}
