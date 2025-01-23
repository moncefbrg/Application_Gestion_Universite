package com.example.demo.services.Etudiant;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.example.demo.entities.Etudiant;
import com.example.demo.entities.Niveau;
import com.example.demo.repositories.IEtudiant;
import com.example.demo.repositories.INiveau;
import com.example.demo.services.fichierexcel.IFichierExcelService;

import jakarta.transaction.Transactional;
@Service
public class EtudiantServiceimpl implements IEtudiantService{
	@Autowired
	private IFichierExcelService ifichierexcelservice;
	@Autowired
	private IEtudiant ietudiant;
	@Autowired
	private INiveau iNiveau;
	@Transactional
	@Override
	public boolean inscrire(File fichier) {
	    // Liste des types de colonnes attendues dans le fichier
	    List<String> liste = new ArrayList<>(Arrays.asList("NUMERIC", "STRING", "STRING", "STRING", "NUMERIC", "STRING"));
	    
	    return inscription(fichier,liste);
	}

	/**
	 * Lit les données d'une ligne du fichier Excel.
	 */
	private List<String> lireLigne(Row row) {
	    List<String> ligneData = new ArrayList<>();
	    for (int colonneIndex = 0; colonneIndex < 6; colonneIndex++) { // 6 colonnes : ID Etudiant, CNE, NOM, PRENOM, ID NIVEAU ACTUEL, TYPE
	        Cell cell = row.getCell(colonneIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
	        ligneData.add(lireValeurCellule(cell));
	    }
	    return ligneData;
	}

	/**
	 * Lit la valeur d'une cellule en fonction de son type.
	 */
	private String lireValeurCellule(Cell cell) {
	    switch (cell.getCellType()) {
	        case STRING:
	            return cell.getStringCellValue();
	        case NUMERIC:
	            if (DateUtil.isCellDateFormatted(cell)) {
	                return cell.getDateCellValue().toString();
	            } else {
	                return String.valueOf((int) cell.getNumericCellValue());
	            }
	        case BOOLEAN:
	            return String.valueOf(cell.getBooleanCellValue());
	        case BLANK:
	            return ""; // Cellule vide
	        default:
	            throw new IllegalStateException("Type de cellule non géré : " + cell.getCellType());
	    }
	}

	/**
	 * Crée et sauvegarde un étudiant à partir des données d'une ligne.
	 */
	@Transactional
	private boolean creerEtSauvegarderEtudiant(List<String> ligneData) {
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

	        // Traiter en fonction du type
	        if ("Inscription".equalsIgnoreCase(type)) {
	            // Cas : Inscription
	            if (etudiantOpt.isPresent()) {
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
	        } else if ("Reinscription".equalsIgnoreCase(type) || "Réinscription".equalsIgnoreCase(type)) {
	            // Cas : Reinscription
	            if (etudiantOpt.isEmpty()) {
	                System.err.println("Erreur : L'étudiant avec l'ID " + idEtudiant + " n'existe pas. Impossible de le réinscrire.");
	                return false;
	            }

	            // Modifier le niveau de l'étudiant existant
	            Etudiant etudiant = etudiantOpt.get();
	            etudiant.setNiveau(niveau); // Mettre à jour le niveau

	            // Sauvegarder les modifications
	            ietudiant.save(etudiant);
	            System.out.println("Étudiant réinscrit : " + etudiant);
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
	public boolean checkFormat(File fichier, List<String> liste) {
	    try (FileInputStream fis = new FileInputStream(fichier);
	         Workbook workbook = new XSSFWorkbook(fis)) {

	        // Vérifier le format des colonnes
	        if (!ifichierexcelservice.formatColonnes(fichier, 6)) {
	            // Si les colonnes ne respectent pas le format, retourner false
	            return false;
	        }

	        Sheet sheet = workbook.getSheetAt(0);  // On travaille avec la première feuille

	        // Parcours des lignes à partir de la ligne 2 (index 1 dans Excel)
	        for (int ligneIndex = 1; ligneIndex <= sheet.getLastRowNum(); ligneIndex++) {
	            Row row = sheet.getRow(ligneIndex);

	            if (row == null) continue;  // Si la ligne est vide, on passe à la suivante

	            // Parcours des colonnes de chaque ligne
	            for (int colIndex = 0; colIndex < liste.size(); colIndex++) {
	                // Récupérer le type attendu depuis la liste
	                String typeAttendu = liste.get(colIndex);

	                // Récupérer le type réel de la cellule
	                String typeCellule = ifichierexcelservice.formatTypeDonnee(fichier, colIndex, ligneIndex);

	                // Comparer le type attendu avec le type réel
	                if (!typeAttendu.equals(typeCellule)) {
	                    // Si les types ne correspondent, retourner false
	                    System.err.println("Erreur à la ligne " + (ligneIndex + 1) + ", colonne " + (colIndex + 1) +
	                            ": Attendu '" + typeAttendu + "', trouvé '" + typeCellule + "'.");
	                    return false;
	                }
	            }
	        }

	        // Si tout est correct, retourner true
	        return true;

	    } catch (Exception e) {
	        // Si une exception est lancée, log l'erreur et retourner false
	        System.err.println("Erreur lors de la vérification du format: " + e.getMessage());
	        return false;
	    }
	}



	@Override
	public Etudiant chercherEtudiantById(Long id) {
		Optional<Etudiant> optionnalEtudiant=Optional.ofNullable(ietudiant.findById(id).orElse(null));
		return optionnalEtudiant.get();
	}

	@Override
	public List<Etudiant> chercherEtudiant(String cne,String nom,String prenom,Long niveau) {
		return ietudiant.findByCneOrNomOrPrenomOrNiveauId(cne, nom, prenom, niveau);
	}

	@Override
	public boolean checkExistanceNiveau(Long id) {
		if(!(iNiveau.findById(id).isEmpty())) {
			return true;
		}else {
			return false;
		}		
	}

	@Override
	public boolean checkNiveau() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean inscription(File fichier,List<String> liste) {
		try {
	        // Vérifier le format du fichier
	        if (!checkFormat(fichier, liste)) {
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
	                List<String> ligneData = lireLigne(row);

	                // Créer et sauvegarder l'étudiant
	                if (!creerEtSauvegarderEtudiant(ligneData)) {
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


	@Override
	public Etudiant modifierEtudiant(Long id,String cne, String nom, String prenom, Long niveau) {
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
			ietudiant.save(e1);
			return e1;
		}else {
			return null;
		}
	}

	@Override
	public List<Etudiant> consulterClasse() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File exporter() {
		// TODO Auto-generated method stub
		return null;
	}

}
