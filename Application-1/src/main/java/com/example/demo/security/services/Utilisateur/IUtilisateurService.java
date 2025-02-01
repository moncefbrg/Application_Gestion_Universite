package com.example.demo.security.services.Utilisateur;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.demo.security.entities.Utilisateur;

public interface IUtilisateurService extends UserDetailsService {
    
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
    public Utilisateur creerUtilisateur(Long id, String username, String password, String roleName);    
    public void changerRoleUtilisateur(String username, String nouveauRole);   
    public String reinitialiserMotDePasse(String username);    
    public void activerDesactiverCompte(String username, boolean enabled);    
    public boolean existsByUsername(String username);    
    public String genererUsernameUnique(String nom, String prenom);
    public void supprimerUtilisateur(Long id);
    public void toggleStatus(Long id);
    public void toggleLock(Long id);
    public Utilisateur getUtilisateurById(Long id);
    public List<Utilisateur> getAllUtilisateurs();
}



