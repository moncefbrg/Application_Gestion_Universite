package com.example.demo.services.fichierexcel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class FichierExcelServiceimpl implements IFichierExcelService {

    @Override
    public boolean formatColonnes(File fichier, int nbr) throws Exception {
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

    // Méthode pour déterminer le type de la cellule sous forme de chaîne de caractères
    private String getCellTypeAsString(Cell cell) {
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

    
}