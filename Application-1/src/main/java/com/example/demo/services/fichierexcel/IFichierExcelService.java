package com.example.demo.services.fichierexcel;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;




public interface IFichierExcelService {
	public boolean checkNbrColonnes(File fichier,int nbr) throws Exception;
	public String formatTypeDonnee(File fichier, int colonne, int ligne) throws Exception;
	public boolean checkFormat(File fichier,List<String> liste,int nbrColonne) throws Exception;
	public List<String> lireLigne(Row row,int nbrColonnes);
	public String getCellTypeAsString(Cell cell);
	public String lireValeurCellule(Cell cell);
	public String verifierFormatFichierExcel(File file) throws IOException;//retourne soit xls , xlsx ,inconnu
	public boolean verifierNotesFichierExcel(File file) throws Exception;
	public File creationFichierDeliberation(String path, Date dateDeliberation, String classe) throws IOException;
    public Cell createCell(Row row, int cellIndex, Object value, CellStyle style);
    public  boolean validateExcelStructure(String filePath);//verfife la structure du fichier de deliberation
    public String getCellValue(Row row, int cellIndex);
    public boolean validateMergedCells(Sheet sheet);
    public boolean validateModuleStructure(Sheet sheet);
    public boolean validateDataContent(Sheet sheet);
    public boolean validateHeaders(Sheet sheet);
    public String getColumnLetter(int columnNumber);
    public void mettreAJourNotesDepuisFichierExcelDeliberation(String cheminFichierExcel, String classe, Date dateDeliberationAttendue) throws IOException;
	public File creationFichierNoteExcel(String classe, String session, String module, String path)throws IOException;//classe notes

}
