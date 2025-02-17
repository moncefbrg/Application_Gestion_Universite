package com.example.demo.services.Etudiant;
//log done
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.example.demo.entities.Classe;
import com.example.demo.entities.Etudiant;
import com.example.demo.entities.Niveau;
import com.example.demo.repositories.IClasse;
import com.example.demo.repositories.IEtudiant;
import com.example.demo.repositories.INiveau;
import com.example.demo.services.fichierexcel.IFichierExcelService;

@Service
public class EtudiantServiceimpl implements IEtudiantService{
	@Autowired
	private IFichierExcelService ifichierexcelservice;
	@Autowired
	private IEtudiant ietudiant;
	@Autowired
	private INiveau iNiveau;
	@Autowired
	private IClasse iClasse;
	
    private static final Logger logger = LoggerFactory.getLogger(EtudiantServiceimpl.class);
	@Transactional
	@Override
	public boolean inscrire(File fichier) throws Exception{
		List<String> listeTypeColonnes=Arrays.asList("NUMERIC","STRING","STRING","STRING","NUMERIC","STRING");
		try {
			if(ifichierexcelservice.checkFormat(fichier, listeTypeColonnes, 6)) {
				logger.info("inscription avec fichier");

			return inscription(fichier,listeTypeColonnes,6);}else {return false;}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	
	}


	/**
	 * Crée et sauvegarde un étudiant à partir des données d'une ligne.
	 */
	@Override @Transactional
	public boolean creerEtSauvegarderEtudiantFromExcel(List<String> ligneData) {
	    try {
	        // Récupérer les données de la ligne
	        Long idEtudiant = Long.parseLong(ligneData.get(0)); // ID Etudiant (Long)
	        String cne = ligneData.get(1); // CNE (String)
	        String nom = ligneData.get(2); // NOM (String)
	        String prenom = ligneData.get(3); // PRENOM (String)
	        Long idNiveau = Long.parseLong(ligneData.get(4)); // ID NIVEAU ACTUEL (Long)
	        String type = ligneData.get(5); // TYPE (String)

	        // Vérifier si le niveau existe
	        Optional<Niveau> niveauOpt = iNiveau.findById(idNiveau);
	        if (niveauOpt.isEmpty()) {
	            System.err.println("Niveau non trouvé pour l'ID : " + idNiveau);
	            return false;
	        }
	        Niveau niveau = niveauOpt.get();

	        // Vérifier si l'étudiant existe déjà
	        Optional<Etudiant> etudiantOpt = ietudiant.findById(idEtudiant);
	        Etudiant etudiantOpt2 = ietudiant.findByCne(cne);


	        // Traiter en fonction du type
	        if ("Inscription".equalsIgnoreCase(type)) {
	            // Cas : Inscription
	            if (etudiantOpt.isPresent() || etudiantOpt2!=null) {
	                System.err.println("Erreur : L'étudiant avec l'ID " + idEtudiant + " existe déjà. Impossible de l'inscrire.");
	                return false;
	            }

	            // Créer un nouvel étudiant
	            Etudiant etudiant = new Etudiant();
	            etudiant.setId(idEtudiant); // ID (Long)
	            etudiant.setCne(cne); // CNE (String)
	            etudiant.setNom(nom); // NOM (String)
	            etudiant.setPrenom(prenom); // PRENOM (String)
	            etudiant.setNiveau(niveau); // Niveau

	            // Sauvegarder l'étudiant dans la base de données
	            ietudiant.save(etudiant);
	            System.out.println("Étudiant inscrit : " + etudiant);
	    		logger.info("Étudiant inscrit : " + etudiant+"au niveau"+idNiveau);

	        } else if ("Reinscription".equalsIgnoreCase(type) || "Réinscription".equalsIgnoreCase(type)) {
	            // Cas : Reinscription
	            if (etudiantOpt.isEmpty()) {
	                System.err.println("Erreur : L'étudiant avec l'ID " + idEtudiant + " n'existe pas. Impossible de le réinscrire.");
	                return false;
	            }if (etudiantOpt.get().getNiveau().getNiveauSuivant()!=niveau) {
	            	System.err.println("Erreur : L'étudiant avec l'ID " + idEtudiant + " son niveau est contradictoire avec son ancien niveau");
	                return false;
	            }
					

	            // Modifier le niveau de l'étudiant existant
	            Etudiant etudiant = etudiantOpt.get();
	            etudiant.setNiveau(niveau); // Mettre à jour le niveau

	            // Sauvegarder les modifications
	            ietudiant.save(etudiant);
	            System.out.println("Étudiant réinscrit : " + etudiant);
	    		logger.info("Étudiant réinscrit : " + etudiant+"au niveau"+idNiveau);

	        } else {
	            System.err.println("Type non reconnu : " + type);
	            return false;
	        }

	        return true;

	    } catch (Exception e) {
	        System.err.println("Erreur lors de la création ou de la modification de l'étudiant : " + e.getMessage());
	        return false;
	    }
	}

	

	@Override
	public Etudiant chercherEtudiantById(Long id) {
		Optional<Etudiant> optionnalEtudiant=Optional.ofNullable(ietudiant.findById(id).orElse(null));
		logger.info("Étudiant cherche : " +id);
		return optionnalEtudiant.get();
	}

	@Override
	public List<Etudiant> chercherEtudiant(String cne,String nom,String prenom,Long niveau) {
		logger.info("chercher des etudiants avec : " +cne+","+nom+","+prenom+","+niveau);

		return ietudiant.findByCneOrNomOrPrenomOrNiveauId(cne, nom, prenom, niveau);
	}

	

	@Override
	public boolean checkNiveau() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean inscription(File fichier,List<String> liste,int nbrColonnes) {
		try {
	        // Vérifier le format du fichier
	        if (!ifichierexcelservice.checkFormat(fichier, liste,nbrColonnes)) {
	            System.err.println("Le format du fichier n'est pas valide.");
	            return false;
	        }

	        // Lire le fichier et traiter les données
	        try (FileInputStream fis = new FileInputStream(fichier);
	             Workbook workbook = new XSSFWorkbook(fis)) {

	            Sheet sheet = workbook.getSheetAt(0);
	            for (int ligneIndex = 1; ligneIndex <= sheet.getLastRowNum(); ligneIndex++) {
	                Row row = sheet.getRow(ligneIndex);
	                if (row == null) continue; // Ignorer les lignes vides

	                // Lire les données de la ligne
	                List<String> ligneData = ifichierexcelservice.lireLigne(row,nbrColonnes);

	                // Créer et sauvegarder l'étudiant
	                if (!creerEtSauvegarderEtudiantFromExcel(ligneData)) {
	                    System.err.println("Erreur lors de la création de l'étudiant à la ligne : " + (ligneIndex + 1));
	                }
	            }
	        }
	    } catch (Exception e) {
	        // Gérer les exceptions
	        System.err.println("Erreur lors de l'inscription des étudiants : " + e.getMessage());
	        return false;
	    }

	    return true;
	}
	
	@Override @Transactional
	public boolean modifierEtudiant(Long id,String cne, String nom, String prenom, Long niveau) {
		Optional<Etudiant> e=ietudiant.findById(id);
		if(!(e.isEmpty())){
			Etudiant e1=e.get();
			if(cne!=null) {e1.setCne(cne);}
			if(nom!=null) {e1.setNom(nom);}
			if(prenom!=null) {e1.setPrenom(prenom);}
			if(niveau!=null) {
				Optional<Niveau> n=iNiveau.findById(niveau);
				e1.setNiveau(n.get());
			}
			logger.info("Étudiant modifie : " +id+"avec :"+cne+","+nom+","+prenom+","+niveau);
			ietudiant.save(e1);
			return true; //si la modif a reussi
		}else {
			return false; 
		}
	}


	@Override
	public File exporter() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public boolean ajouterEtudiant(Long id, String nom, String prenom, String cne, String aliasNiveau, String nomClasse) throws Exception {
	    // Vérifier si l'étudiant existe déjà par son CNE (éviter les doublons)
		Etudiant etudiant1=ietudiant.findByCne(cne);
	    if (!(etudiant1==null)) {
	        throw new Exception("Un étudiant avec le CNE '" + cne + "' existe déjà.");
	    }

	    if(!(nomClasse==null)) {// Trouver la classe par son nom
		    iClasse.findByNom(nomClasse)
		            .orElseThrow(() -> new Exception("Classe avec le nom '" + nomClasse + "' non trouvée."));
}
	    // Trouver le niveau par son alias
	    Niveau niveau = iNiveau.findByAlias(aliasNiveau)
	    		.orElseThrow(() -> new Exception("Niveau avec l'alias '" + aliasNiveau + "' non trouvé."));
	    

	    // Créer l'étudiant
	    Etudiant etudiant = Etudiant.builder()
	            .id(id)
	            .nom(nom)
	            .prenom(prenom)
	            .cne(cne)
	            .classe(nomClasse==null?null:iClasse.findByNom(nomClasse).get())
	            .niveau(niveau)
	            .modules(niveau.getModules()) // Associer les modules du niveau à l'étudiant
	            .build();

	    // Enregistrer l'étudiant
	    ietudiant.save(etudiant);
		logger.info("Étudiant ajoute : " + id+","+nom+","+prenom+","+cne+","+aliasNiveau+","+nomClasse);

	    return true; // Retourner true si l'étudiant a été ajouté avec succès
	}


	@Override
	@Transactional
	public boolean supprimerEtudiant(Long id) {
	    // Vérifier que l'ID n'est pas null
	    if (id == null) {
	        throw new IllegalArgumentException("L'ID de l'étudiant ne peut pas être null.");
	    }

	    // Rechercher l'étudiant par son ID
	    Optional<Etudiant> etudiantOptional = ietudiant.findById(id);
	    if (etudiantOptional.isEmpty()) {
	        throw new RuntimeException("L'étudiant avec l'ID " + id + " n'existe pas.");
	    }

	    // Récupérer l'étudiant à supprimer
	    Etudiant etudiant = etudiantOptional.get();

	    // Supprimer l'étudiant
	    ietudiant.delete(etudiant);
		logger.info("Étudiant supprime : " + id);


	    return true; // Retourne true si la suppression est réussie
	}
	@Override
	@Transactional
	public boolean associerEtudiantClasse(Classe classe, Etudiant etudiant) throws Exception {
	    // Vérification des nullités
	    if (classe == null) {
	        throw new RuntimeException("La classe ne peut pas être null.");
	    }
	    if (etudiant == null) {
	        throw new RuntimeException("L'étudiant ne peut pas être null.");
	    }

	    // Vérification de l'existence de la classe dans la base de données
	    Classe classeBD = iClasse.findById(classe.getId())
	            .orElseThrow(() -> new RuntimeException("La classe avec l'ID " + classe.getId() + " n'existe pas."));

	    // Vérification de l'existence de l'étudiant dans la base de données
	    Etudiant etudiantBD = ietudiant.findById(etudiant.getId())
	            .orElseThrow(() -> new RuntimeException("L'étudiant avec l'ID " + etudiant.getId() + " n'existe pas."));

	    // Vérification de niveau de l'étudiant par rapport a la classe
	    if (etudiantBD.getNiveau() != classeBD.getNiveau() || etudiantBD.getNiveau().getNiveauSuivant() != classeBD.getNiveau() ) {
	        throw new RuntimeException("Incoherance de niveau.");
	    }

	    // Association de l'étudiant à la classe
	    etudiantBD.setClasse(classeBD);

	    // Sauvegarde des modifications
	    ietudiant.save(etudiantBD);
		logger.info("Étudiant"+etudiant.getId()+" associe au classe : "+classe.getNom());

	    return true;
	}

	@Override
	@Transactional
	public boolean separerEtudiantClasse(Classe classe, Etudiant etudiant) throws Exception {
	    // Vérification des nullités
	    if (classe == null) {
	        throw new RuntimeException("La classe ne peut pas être null.");
	    }
	    if (etudiant == null) {
	        throw new RuntimeException("L'étudiant ne peut pas être null.");
	    }

	    // Vérification de l'existence de la classe dans la base de données
	    Classe classeBD = iClasse.findById(classe.getId())
	            .orElseThrow(() -> new RuntimeException("La classe avec l'ID " + classe.getId() + " n'existe pas."));

	    // Vérification de l'existence de l'étudiant dans la base de données
	    Etudiant etudiantBD = ietudiant.findById(etudiant.getId())
	            .orElseThrow(() -> new RuntimeException("L'étudiant avec l'ID " + etudiant.getId() + " n'existe pas."));

	    // Vérification que l'étudiant est bien associé à cette classe
	    if (etudiantBD.getClasse() == null || !etudiantBD.getClasse().getId().equals(classeBD.getId())) {
	        throw new RuntimeException("L'étudiant n'est pas associé à cette classe.");
	    }

	    // Séparation de l'étudiant et de la classe
	    etudiantBD.setClasse(null);

	    // Sauvegarde des modifications
	    ietudiant.save(etudiantBD);
		logger.info("Étudiant"+etudiant.getId()+" separe au classe : "+classe.getNom());


	    return true;
	}


	@Override
	public List<Etudiant> getAllEtudiants() {
		return ietudiant.findAll();
	}


	@Override
	public Optional<Etudiant> findById(Long id) {
		return ietudiant.findById(id);
	}


}