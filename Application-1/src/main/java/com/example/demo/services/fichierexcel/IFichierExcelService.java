package com.example.demo.services.fichierexcel;

import java.io.File;

public interface IFichierExcelService {
	public boolean formatColonnes(File fichier,int nbr) throws Exception;
	public String formatTypeDonnee(File fichier, int colonne, int ligne) throws Exception;

}
