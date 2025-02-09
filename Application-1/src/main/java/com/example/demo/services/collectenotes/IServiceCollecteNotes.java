package com.example.demo.services.collectenotes;

import java.io.File;
import java.util.List;

public interface IServiceCollecteNotes {
	File genererFichierCollecteNotes(String session, String classe, String module,String path) throws Exception; //genere un fichier excel pour la collecte de notes pour module
	File genererFichierDeliberation(String niveau, String session) throws Exception; //un fichier excel des notes finales pour un niveau et une session
	File genererArchiveFichiersModulesPourNiveau(String niveau, String session) throws Exception; //archive contenant tous les fichiers Excel de collecte de notes pour tous les modules d'un niveau donné
	List<Double> recupererNotesEtudiant(Long idEtudiant) throws Exception;
	void mettreAJourNotesEtudiantDansFichier(File fichier, Long idetudiant, double nouvelleNote) throws Exception;	
	List<Long> recupererIdsEtudiantsClasse(String nomClasse) throws Exception;

}
