package com.example.demo.services.classe;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.Classe;
import com.example.demo.entities.Etudiant;
import com.example.demo.entities.Niveau;
import com.example.demo.repositories.IClasse;
import com.example.demo.repositories.IEtudiant;
import com.example.demo.repositories.INiveau;
@Service
public class ClasseServiceImpl implements IClasseService{
	@Autowired
	private IClasse iClasse;
	@Autowired
	private INiveau iNiveau;
	@Autowired
	private IEtudiant iEtudiant;
    private static final Logger logger = LoggerFactory.getLogger(ClasseServiceImpl.class);

	@Override @Transactional
	public Classe creerClasse(String nom) {
	    // Vérifier si une classe avec ce nom existe déjà
	    if (iClasse.findByNom(nom).isPresent()) {
	        throw new RuntimeException("Une classe avec le nom " + nom + "  existe déjà.");
	    }

	    // Créer une nouvelle classe
	    Classe classe = new Classe();
	    classe.setNom(nom);

	    // Sauvegarder la classe
	    iClasse.save(classe);
	    logger.info("la classe "+nom+" a ete cree");

	    // Vérifier si la classe a été correctement sauvegardée
	    return classe;
	}
	
	@Override @Transactional
	public boolean supprimerClasse(String nom) {
		iClasse.delete(iClasse.findByNom(nom).get());
	    logger.info("la classe "+nom+" a ete supprimee");

		return true;
	};
	@Override
	@Transactional
	public boolean modifierClasse(String nom, String nouveauNom) {
	    // Vérifier que les noms ne sont pas null ou vides
	    if (nom == null || nom.trim().isEmpty()) {
	        throw new IllegalArgumentException("Le nom de la classe ne peut pas être null ou vide.");
	    }
	    if (nouveauNom == null || nouveauNom.trim().isEmpty()) {
	        throw new IllegalArgumentException("Le nouveau nom de la classe ne peut pas être null ou vide.");
	    }

	    // Rechercher la classe par son nom
	    Optional<Classe> classeOptional = iClasse.findByNom(nom);
	    if (classeOptional.isEmpty()) {
	        throw new RuntimeException("La classe avec le nom " + nom + " n'existe pas.");
	    }

	    // Vérifier si une classe avec le nouveau nom existe déjà
	    if (iClasse.findByNom(nouveauNom).isPresent()) {
	        throw new RuntimeException("Une classe avec le nom " + nouveauNom + " existe déjà.");
	    }

	    // Modifier le nom de la classe
	    Classe classe = classeOptional.get();
	    classe.setNom(nouveauNom);
	    logger.info("la classe "+nom+" a devenu "+nouveauNom);


	    // Pas besoin d'appeler iClasse.save(classe) car @Transactional le gère automatiquement

	    return true; // Retourne true si la modification est réussie
	}

	@Override
	@Transactional
	public boolean ajouterEtudiantClasse(String nom, Long id) {
	    // Vérifier que les entrées ne sont pas null ou invalides
	    if (nom == null || nom.trim().isEmpty()) {
	        throw new IllegalArgumentException("Le nom de la classe ne peut pas être null ou vide.");
	    }
	    if (id == null) {
	        throw new IllegalArgumentException("L'ID de l'étudiant ne peut pas être null.");
	    }

	    // Rechercher la classe par son nom
	    Optional<Classe> classeOptional = iClasse.findByNom(nom);
	    if (classeOptional.isEmpty()) {
	        throw new RuntimeException("La classe avec le nom " + nom + " n'existe pas.");
	    }

	    // Rechercher l'étudiant par son ID
	    Optional<Etudiant> etudiantOptional = iEtudiant.findById(id);
	    if (etudiantOptional.isEmpty()) {
	        throw new RuntimeException("L'étudiant avec l'ID " + id + " n'existe pas.");
	    }

	    // Récupérer les objets
	    Classe classe = classeOptional.get();
	    Etudiant etudiant = etudiantOptional.get();

	    // Vérifier si l'étudiant est déjà dans la classe
	    if (classe.getEtudiants().contains(etudiant)) {
	        throw new RuntimeException("L'étudiant avec l'ID " + id + " est déjà dans la classe " + nom + ".");
	    }

	    // Ajouter l'étudiant à la classe
	    classe.getEtudiants().add(etudiant);

	    // Sauvegarder la classe (automatiquement géré par @Transactional)
	    iClasse.save(classe);
	    logger.info("l'etudiant "+id+" a ete associe a la classe "+nom);


	    return true; // Retourne true si l'ajout est réussi
	}

	@Override
	@Transactional
	public boolean supprimerEtudiantClasse(String nom, Long id) {
	    // Vérifier que les entrées ne sont pas null ou invalides
	    if (nom == null || nom.trim().isEmpty()) {
	        throw new IllegalArgumentException("Le nom de la classe ne peut pas être null ou vide.");
	    }
	    if (id == null) {
	        throw new IllegalArgumentException("L'ID de l'étudiant ne peut pas être null.");
	    }

	    // Rechercher la classe par son nom
	    Optional<Classe> classeOptional = iClasse.findByNom(nom);
	    if (classeOptional.isEmpty()) {
	        throw new RuntimeException("La classe avec le nom " + nom + " n'existe pas.");
	    }

	    // Rechercher l'étudiant par son ID
	    Optional<Etudiant> etudiantOptional = iEtudiant.findById(id);
	    if (etudiantOptional.isEmpty()) {
	        throw new RuntimeException("L'étudiant avec l'ID " + id + " n'existe pas.");
	    }

	    // Récupérer les objets
	    Classe classe = classeOptional.get();
	    Etudiant etudiant = etudiantOptional.get();

	    // Vérifier si l'étudiant est dans la classe
	    if (!classe.getEtudiants().contains(etudiant)) {
	        throw new RuntimeException("L'étudiant avec l'ID " + id + " n'est pas dans la classe " + nom + ".");
	    }

	    // Supprimer l'étudiant de la classe
	    classe.getEtudiants().remove(etudiant);

	    // Sauvegarder la classe (automatiquement géré par @Transactional)
	    iClasse.save(classe);
	    logger.info("l'etudiant "+id+" a ete suprime de la classe"+nom);


	    return true; // Retourne true si la suppression est réussie
	}

	@Override
	public List<Etudiant> consulterClasse(String nom) {
	    // Vérifier que le nom n'est pas null ou vide
	    if (nom == null || nom.trim().isEmpty()) {
	        throw new IllegalArgumentException("Le nom de la classe ne peut pas être null ou vide.");
	    }

	    // Rechercher la classe par son nom
	    Optional<Classe> classeOptional = iClasse.findByNom(nom);
	    if (classeOptional.isEmpty()) {
	        throw new RuntimeException("La classe avec le nom " + nom + " n'existe pas.");
	    }

	    // Récupérer la classe
	    Classe classe = classeOptional.get();

	    // Récupérer la liste des étudiants
	    List<Etudiant> etudiants = classe.getEtudiants();
	    logger.info("Consultation de la classe "+nom);

	    // Retourner la liste des étudiants (peut être vide mais pas null)
	    return etudiants != null ? etudiants : Collections.emptyList();
	}

	@Override
	@Transactional
	public boolean associerClasseNiveau(Classe classe, Niveau niveau) throws Exception {
	    // Vérification des nullités
	    if (classe == null) {
	        throw new RuntimeException("La classe ne peut pas être null.");
	    }
	    if (niveau == null) {
	        throw new RuntimeException("Le niveau ne peut pas être null.");
	    }

	    // Vérification de l'existence de la classe dans la base de données
	    Classe classeBD = iClasse.findById(classe.getId())
	            .orElseThrow(() -> new RuntimeException("La classe avec l'ID " + classe.getId() + " n'existe pas."));

	    // Vérification de l'existence du niveau dans la base de données
	    Niveau niveauBD = iNiveau.findById(niveau.getId())
	            .orElseThrow(() -> new RuntimeException("Le niveau avec l'ID " + niveau.getId() + " n'existe pas."));

	    // Vérification que la classe n'est pas déjà associée à un autre niveau
	    if (classeBD.getNiveau() != null) {
	        throw new RuntimeException("La classe est déjà associée à un autre niveau.");
	    }

	    // Association de la classe au niveau
	    classeBD.setNiveau(niveauBD);

	    // Sauvegarde des modifications
	    iClasse.save(classeBD);
	    logger.info("la classe "+classe.getNom()+" a ete associe au niveau "+niveau.getAlias());


	    return true;
	}
	@Override
	@Transactional
	public boolean separerClasseNiveau(Classe classe, Niveau niveau) throws Exception {
	    // Vérification des nullités
	    if (classe == null) {
	        throw new RuntimeException("La classe ne peut pas être null.");
	    }
	    if (niveau == null) {
	        throw new RuntimeException("Le niveau ne peut pas être null.");
	    }

	    // Vérification de l'existence de la classe dans la base de données
	    Classe classeBD = iClasse.findById(classe.getId())
	            .orElseThrow(() -> new RuntimeException("La classe avec l'ID " + classe.getId() + " n'existe pas."));

	    // Vérification de l'existence du niveau dans la base de données
	    Niveau niveauBD = iNiveau.findById(niveau.getId())
	            .orElseThrow(() -> new RuntimeException("Le niveau avec l'ID " + niveau.getId() + " n'existe pas."));

	    // Vérification que la classe est bien associée au niveau
	    if (classeBD.getNiveau() == null || !classeBD.getNiveau().getId().equals(niveauBD.getId())) {
	        throw new RuntimeException("La classe n'est pas associée à ce niveau.");
	    }

	    // Séparation de la classe et du niveau
	    classeBD.setNiveau(null);

	    // Sauvegarde des modifications
	    iClasse.save(classeBD);
	    logger.info("la classe "+classe.getNom()+" a ete separer du niveau "+niveau.getAlias());


	    return true;
	}
	
	public List<Etudiant> getTousLesEtudiants() {
	    List<Classe> classes = iClasse.findAll(); // Récupérer toutes les classes
	    List<Etudiant> listEtudiants = new ArrayList<>();

	    for (Classe classe : classes) {
	        listEtudiants.addAll(classe.getEtudiants()); // Ajouter les étudiants de chaque classe
	    }

	    return listEtudiants;
	}

	@Override
	public List<Classe> getAllClasses() {
		return iClasse.findAll();
	}

	@Override
	public Classe getClasseById(Long id) {
		return iClasse.findById(id)
				.orElseThrow(() -> new RuntimeException("Classe non trouvée avec l'ID : " + id));
	}

	@Override
	public Classe getClasseByNom(String nom) {
		return iClasse.findByNom(nom)
				.orElseThrow(() -> new RuntimeException("Classe non trouvée"));
	}

	@Override
	public void save(Classe classe) {
		iClasse.save(classe);
	}


	
}
