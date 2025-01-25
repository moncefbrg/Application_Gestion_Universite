package com.example.demo.services.fichierexcel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
    
    @Override
    public String verifierFormatFichierExcel(File file) throws IOException {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("Le fichier n'existe pas ou est null.");
        }

        // Utilisation du try-with-resources pour garantir la fermeture automatique du flux
        try (FileInputStream fis = new FileInputStream(file)) {
            try(Workbook workbook = new XSSFWorkbook(fis);){
                // Essayer de lire le fichier comme un fichier .xlsx
                return "XLSX";
            } catch (IOException e) {
                // Si ce n'est pas un fichier .xlsx, essayer de lire comme un fichier .xls
                try(Workbook workbook = new HSSFWorkbook(fis);){
                    return "XLS";
                } catch (IOException ex) {
                    // Si ni .xlsx ni .xls, retourner "INCONNU"
                    return "INCONNU";
                }
            }
        }
    }
    
    @Override
    public boolean verifierNotesFichierExcel(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            // Commencer à la ligne 5 
            for (int i = 4; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue; // Ignorer les lignes vides
                }

                // Colonnes ELEMENT 1 (index 4) et ELEMENT 2 (index 5)
                Cell cell4 = row.getCell(4); // ELEMENT 1
                Cell cell5 = row.getCell(5); // ELEMENT 2

                // Vérifier si les cellules sont null ou non numériques
                if (cell4 == null || cell5 == null) {
                    throw new IllegalArgumentException("Erreur à la ligne " + (i + 1) + ": Les cellules des notes sont manquantes.");
                }
                if (cell4.getCellType() != CellType.NUMERIC || cell5.getCellType() != CellType.NUMERIC) {
                    throw new IllegalArgumentException("Erreur à la ligne " + (i + 1) + ": Les notes doivent être des valeurs numériques.");
                }

                double note4 = cell4.getNumericCellValue();
                double note5 = cell5.getNumericCellValue();

                // Vérifier les conditions sur les notes
                if (note4 <= 0 || note5 <= 0) {
                    throw new IllegalArgumentException("Erreur à la ligne " + (i + 1) + ": Les notes doivent être supérieures à 0.");
                }
                if (note4 > 20 || note5 > 20) {
                    throw new IllegalArgumentException("Erreur à la ligne " + (i + 1) + ": Les notes ne doivent pas dépasser 20.");
                }
            }
        } catch (IOException e) {
            throw new IOException("Erreur lors de la lecture du fichier Excel.", e);
        }

        return true; // Si toutes les notes sont valides
    }
    
    @Override
    public File createExcelFile(String path, String anneeUniversitaire, String dateDeliberation, String classe,
            List<String> modules, List<String> enseignants, List<String> etudiants) throws IOException {
    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("Délibération");

    // Créer une police en gras pour les en-têtes
    Font headerFont = workbook.createFont();
    headerFont.setBold(true);
    headerFont.setFontHeightInPoints((short) 12);
    CellStyle headerStyle = workbook.createCellStyle();
    headerStyle.setFont(headerFont);
    headerStyle.setBorderBottom(BorderStyle.THIN);
    headerStyle.setBorderTop(BorderStyle.THIN);
    headerStyle.setBorderLeft(BorderStyle.THIN);
    headerStyle.setBorderRight(BorderStyle.THIN);
    headerStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    headerStyle.setAlignment(HorizontalAlignment.CENTER);  // Alignement horizontal au centre
    headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);  // Alignement vertical au centre
  
    
 // Créer style deds cellules vides
    CellStyle celluleVideStyle = workbook.createCellStyle();
    celluleVideStyle.setBorderBottom(BorderStyle.THIN);
    celluleVideStyle.setBorderTop(BorderStyle.THIN);
    celluleVideStyle.setBorderLeft(BorderStyle.THIN);
    celluleVideStyle.setBorderRight(BorderStyle.THIN);
    celluleVideStyle.setAlignment(HorizontalAlignment.CENTER);

    // Créer une police normale pour les données
    Font dataFont = workbook.createFont();
    dataFont.setFontHeightInPoints((short) 11);
    CellStyle dataStyle = workbook.createCellStyle();
    dataStyle.setFont(dataFont);
    dataStyle.setBorderBottom(BorderStyle.THIN);
    dataStyle.setBorderTop(BorderStyle.THIN);
    dataStyle.setBorderLeft(BorderStyle.THIN);
    dataStyle.setBorderRight(BorderStyle.THIN);
    dataStyle.setAlignment(HorizontalAlignment.CENTER);

    // Créer la ligne d'en-tête
    Row headerRow = sheet.createRow(0);
    createCell(headerRow, 0, "Annee universitaire", headerStyle);
    createCell(headerRow, 1, anneeUniversitaire, celluleVideStyle);
    createCell(headerRow, 2, "Date deliberation", headerStyle);
    createCell(headerRow, 3, dateDeliberation, celluleVideStyle);
    
 // Créer la deuxieme ligne
    Row ligneVide = sheet.createRow(1);
    createCell(ligneVide, 0, null, celluleVideStyle);
    createCell(ligneVide, 1, null, celluleVideStyle);
    createCell(ligneVide, 2, null, celluleVideStyle);
    createCell(ligneVide, 3, null, celluleVideStyle);
    

    Row classRow = sheet.createRow(2);
    createCell(classRow, 0, "Classe", headerStyle);
    createCell(classRow, 1, classe, celluleVideStyle);
    createCell(classRow, 2, null, headerStyle);
    createCell(classRow, 3, null, headerStyle);

    // Créer les en-têtes des modules
    Row moduleHeaderRow = sheet.createRow(4);
    createCell(moduleHeaderRow, 0, "ID ETUDIANT", headerStyle);
    createCell(moduleHeaderRow, 1, "CNE", headerStyle);
    createCell(moduleHeaderRow, 2, "NOM", headerStyle);
    createCell(moduleHeaderRow, 3, "PRENOM", headerStyle);
    
 // Créer les en-têtes vide dessous du 6ieme ligne
    Row enteteVide = sheet.createRow(5);
    createCell(enteteVide, 0, null, headerStyle);
    createCell(enteteVide, 1, null, headerStyle);
    createCell(enteteVide, 2, null, headerStyle);
    createCell(enteteVide, 3, null, headerStyle);

    int cellIndex = 4;
    for (int i = 0; i < modules.size(); i++) {
        createCell(moduleHeaderRow, cellIndex, modules.get(i) + " (" + enseignants.get(i) + ")", headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(4, 4, cellIndex, cellIndex + 1)); // Fusionner avec la cellule à droite
        cellIndex += 2;
        createCell(moduleHeaderRow, cellIndex++, "moyenne", headerStyle);
        createCell(moduleHeaderRow, cellIndex++, "validation", headerStyle);
    }
    createCell(moduleHeaderRow, cellIndex, "Moyenne generale", headerStyle);
    createCell(moduleHeaderRow, cellIndex + 1, "Rang", headerStyle);

    // Ajouter les noms des éléments sous les modules
    Row elementHeaderRow = sheet.createRow(5);
    cellIndex = 4;
    for (int i = 0; i < modules.size(); i++) {
        createCell(elementHeaderRow, cellIndex++, "nom element 1", headerStyle);
        createCell(elementHeaderRow, cellIndex++, "nom element 2", headerStyle);
        createCell(elementHeaderRow, cellIndex++, "", headerStyle);
        createCell(elementHeaderRow, cellIndex++, "", headerStyle);
    }

    // Fusionner les cellules "ID ETUDIANT", "CNE", "NOM", "PRENOM", "Moyenne generale", "Rang", "moyenne", "validation"
    sheet.addMergedRegion(new CellRangeAddress(4, 5, 0, 0)); // ID ETUDIANT
    sheet.addMergedRegion(new CellRangeAddress(4, 5, 1, 1)); // CNE
    sheet.addMergedRegion(new CellRangeAddress(4, 5, 2, 2)); // NOM
    sheet.addMergedRegion(new CellRangeAddress(4, 5, 3, 3)); // PRENOM
    
    sheet.addMergedRegion(new CellRangeAddress(4, 5, cellIndex, cellIndex)); // Moyenne generale
    sheet.addMergedRegion(new CellRangeAddress(4, 5, cellIndex + 1, cellIndex + 1)); // Rang

    // Ajouter les données des étudiants
    int rowIndex = 6;
    for (String etudiant : etudiants) {
        Row row = sheet.createRow(rowIndex++);
        createCell(row, 0, "ID1", dataStyle);
        createCell(row, 1, "CNE", dataStyle);
        createCell(row, 2, "NOM", dataStyle);
        createCell(row, 3, etudiant, dataStyle);

        cellIndex = 4;
        for (int i = 0; i < modules.size(); i++) {
            createCell(row, cellIndex++, "12", dataStyle);
            createCell(row, cellIndex++, "12", dataStyle);
            createCell(row, cellIndex++, "12", dataStyle);
            createCell(row, cellIndex++, "V", dataStyle);
        }
        createCell(row, cellIndex, "18", dataStyle);
        createCell(row, cellIndex + 1, "20", dataStyle);
    }

    // Ajuster la largeur des colonnes
    for (int i = 0; i < cellIndex + 2; i++) {
        sheet.autoSizeColumn(i);
    }

    // Créer le dossier s'il n'existe pas
    File resourcesDir = new File(path);
    if (!resourcesDir.exists()) {
        resourcesDir.mkdirs();
    }

    // Générer un nom de fichier unique
    String baseFileName = "Fichier_Deliberation";
    String fileExtension = ".xlsx";
    File file = new File(path + File.separator + baseFileName + fileExtension);

    int counter = 1;
    while (file.exists()) {
        file = new File(path + File.separator + baseFileName + "_" + counter + fileExtension);
        counter++;
    }

    // Écrire le fichier Excel
    try (FileOutputStream fileOut = new FileOutputStream(file)) {
        workbook.write(fileOut);
    } finally {
        workbook.close();
    }

    return file;
}
    @Override
    public Cell createCell(Row row, int column, String value, CellStyle style) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
        cell.setCellStyle(style);
        return cell;
    }

}