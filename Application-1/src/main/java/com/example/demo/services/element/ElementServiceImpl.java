package com.example.demo.services.element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.Element;
import com.example.demo.entities.Module;
import com.example.demo.repositories.IElement;
import com.example.demo.repositories.IModule;

@Service
public class ElementServiceImpl implements IElementService {

    @Autowired
    private IElement elementRepository;
    @Autowired
    private IModule iModule;
    private static final Logger logger = LoggerFactory.getLogger(ElementServiceImpl.class);
    @Override
    @Transactional
    public Element creerElement(Long id, String nom,Module module) {
        // Vérifier si un élément avec le même ID existe déjà
        if (elementRepository.existsById(id)) {
            throw new RuntimeException("Un élément avec l'ID " + id + " existe déjà.");
        }

        // Vérifier si un élément avec le même nom existe déjà
        if (elementRepository.existsByNom(nom)) {
            throw new RuntimeException("Un élément avec le nom " + nom + " existe déjà.");
        }
        if (iModule.findById(module.getId()).isEmpty()) {
	        throw new IllegalArgumentException("Le module avec le nom "+module.getNom()+" n'existe pas");
	    }

        // Utilisation du Builder pour créer un nouvel élément
        Element element = Element.builder()
                .id(id)
                .nom(nom)
                .module(module)
                .build();
        logger.info("creation d'element"+element.getNom()+"pour module"+module.getNom());
        // Sauvegarder l'élément dans la base de données
        return elementRepository.save(element);
    }

    @Override
    @Transactional
    public Element modifierElement(Long id, String ancienNom, String nouveauNom) {
        // Récupérer l'élément existant par son ID
        Element element = elementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Élément non trouvé avec l'ID : " + id));
        
        // Vérifier si l'ancien nom correspond
        if (!element.getNom().equals(ancienNom)) {
            throw new RuntimeException("Le nom de l'élément ne correspond pas à l'ancien nom fourni.");
        }
        
        // Utilisation du Builder pour mettre à jour l'élément
        Element elementModifie = Element.builder()
                .id(element.getId()) // Conserver le même ID
                .nom(nouveauNom) // Nouveau nom
                .module(element.getModule()) // Conserver le module existant
                .ResultatsElements(element.getResultatsElements()) // Conserver les résultats existants
                .build();
        
        // Sauvegarder les modifications
        logger.info("Modification d'element"+ancienNom+" avec "+nouveauNom);

        return elementRepository.save(elementModifie);
    }

    @Override
    @Transactional
    public boolean supprimerElement(Long id) {
        // Vérifier si l'élément existe
        if (elementRepository.existsById(id)) {
            // Supprimer l'élément
            elementRepository.deleteById(id);
            logger.info("suppression d'element"+id);

            return true;
        } else {
            // Retourner false si l'élément n'existe pas
            return false;
        }
    }

	@Override
	public List<Element> getElementsByModule(Module module) {
		return elementRepository.findByModule(module);
	}

	@Override
	public List<Element> getAllElements() {
		return elementRepository.findAll();
	}

	@Override
	public Optional<Element> findById(Long id) {
		return elementRepository.findById(id);
	}
}