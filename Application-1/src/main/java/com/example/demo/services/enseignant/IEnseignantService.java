package com.example.demo.services.enseignant;
import com.example.demo.entities.Enseignant;
import com.example.demo.entities.Module;
import java.util.List;
import java.util.Optional;

public interface IEnseignantService {
	public boolean creeEnseignant(Long id,String nom,String prenom,String cni,List<Module>modules);
	public boolean supprimerEnseignant(Long id);
	public boolean modifierEnseignant(Long id,String nom,String prenom,String cni,List<Module>modules);
	public List<Enseignant> getAllEnseignants();
	public Optional<Enseignant> getEnseignantById(Long id);
	public List<Enseignant> findAll();
	public Optional<Enseignant> findById(Long id);


}
