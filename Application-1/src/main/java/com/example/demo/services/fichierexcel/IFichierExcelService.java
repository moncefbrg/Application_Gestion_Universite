package com.example.demo.services.fichierexcel;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;




public interface IFichierExcelService {
	public boolean checkNbrColonnes(File fichier,int nbr) throws Exception;
	public String formatTypeDonnee(File fichier, int colonne, int ligne) throws Exception;
	public boolean checkFormat(File fichier,List<String> liste,int nbrColonne) throws Exception;
	public List<String> lireLigne(Row row,int nbrColonnes);
	public String getCellTypeAsString(Cell cell);
	public String lireValeurCellule(Cell cell);
	public String verifierFormatFichierExcel(File file) throws IOException;//retourne soit xls , xlsx ,inconnu
	public boolean verifierNotesFichierExcel(File file) throws Exception;
	public File createExcelFile(String path, String anneeUniversitaire, String dateDeliberation, String classe,
            List<String> modules, List<String> enseignants, List<String> etudiants) throws IOException;
    public Cell createCell(Row row, int column, String value, CellStyle style);
	
}
