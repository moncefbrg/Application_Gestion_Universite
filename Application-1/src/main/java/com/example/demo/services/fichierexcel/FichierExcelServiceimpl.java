package com.example.demo.services.fichierexcel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FichierExcelServiceimpl implements IFichierExcelService {
	
	@Override
	public boolean checkFormat(File fichier, List<String> liste,int nbrColonne) {
	    try (FileInputStream fis = new FileInputStream(fichier);
	         Workbook workbook = new XSSFWorkbook(fis)) {

	        // Vérifier le format des colonnes
	        if (!checkNbrColonnes(fichier, nbrColonne)) {
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
	                String typeCellule = formatTypeDonnee(fichier, colIndex, ligneIndex);

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
    public boolean checkNbrColonnes(File fichier, int nbr) throws Exception {
        try (FileInputStream fis = new FileInputStream(fichier);
             Workbook workbook = new XSSFWorkbook(fis)) {

            // Récupérer la première feuille
            Sheet sheet = workbook.getSheetAt(0);

            // Vérifier le nombre de colonnes
            Row firstRow = sheet.getRow(0);
            if (firstRow == null) {
                throw new IllegalArgumentException("La première ligne est vide.");
            }

            int nbrReelColonnes = firstRow.getPhysicalNumberOfCells();
            if (nbrReelColonnes != nbr) {
                throw new IllegalArgumentException("Nombre de colonnes incorrect. Attendu : " + nbr + ", Trouvé : " + nbrReelColonnes);
            }

        } catch (IOException e) {
            throw new Exception("Erreur lors de la lecture du fichier : " + e.getMessage(), e);
        }
        return true;
    }

    @Override
    public String formatTypeDonnee(File fichier, int colonne, int ligne) throws Exception {
        try (FileInputStream fis = new FileInputStream(fichier);
             Workbook workbook = new XSSFWorkbook(fis)) {

            // Récupérer la première feuille
            Sheet sheet = workbook.getSheetAt(0);

            // Vérifier la ligne spécifiée
            Row row = sheet.getRow(ligne);
            if (row == null) {
                throw new IllegalArgumentException("La ligne " + ligne + " est vide.");
            }

            // Vérifier la cellule spécifiée
            Cell cell = row.getCell(colonne, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            return getCellTypeAsString(cell);

        } catch (IOException e) {
            throw new Exception("Erreur lors de la lecture du fichier : " + e.getMessage(), e);
        }
    }
    @Override
    // Méthode pour déterminer le type de la cellule sous forme de chaîne de caractères
    public String getCellTypeAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return "STRING";
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return "DATE";
                } else {
                    return "NUMERIC";
                }
            case BOOLEAN:
                return "BOOLEAN";
            case FORMULA:
                return "FORMULA";
            case BLANK:
                return "BLANK";
            default:
                return "UNKNOWN";
        }
    }
    @Override
    public List<String> lireLigne(Row row,int nbrColonnes) {
	    List<String> ligneData = new ArrayList<>();
	    for (int colonneIndex = 0; colonneIndex < nbrColonnes; colonneIndex++) { 
	        Cell cell = row.getCell(colonneIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
	        ligneData.add(lireValeurCellule(cell));
	    }
	    return ligneData;
	}
    @Override
    public String lireValeurCellule(Cell cell) {
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
    
}