package com.example.demo.security.services.Utilisateur;

import java.util.Collections;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.security.repositories.IPersonne;
import com.example.demo.security.repositories.IRole;
import com.example.demo.security.repositories.IUtilisateur;

import org.springframework.transaction.annotation.Transactional;

import com.example.demo.security.entities.Role;
import com.example.demo.security.entities.Utilisateur;

@Service
public class UtilisateurService implements UserDetailsService {
	@Autowired
	private IUtilisateur iutilisateur;
	
	@Autowired
	private IPersonne ipersonne;
	
	@Autowired
	private IRole irole;
	
	@Autowired
	@Lazy
	private PasswordEncoder passwordEncoder; //pour encoder le mdp avant de l'enregistrer dans la bd


	@Override
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//rechercher l'utilisateur dans la BD
		Utilisateur utilisateur=iutilisateur.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec le nom d'utilisateur : " + username));
		
		//retourner un objet UserDetails pour Spring Security
		return new User(
				utilisateur.getUsername(), 
				utilisateur.getPassword(),
				Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + utilisateur.getRole().getNom())));
	}
	
	@Transactional
	public Utilisateur creerUtilisateur(Long id,String username, String password, String roleName) {
		//on verifie si l'utilisateur existe deja
		if(iutilisateur.findByUsername(username).isPresent()) {
			throw new RuntimeException("Ce nom d'utilisateur est déjà pris !");
		}
		
		Role role= irole.findByNom(roleName)
				.orElseThrow(() -> new RuntimeException("Rôle non trouvé"));
		
	    // Vérifier si le rôle ADMIN_USER est déjà attribué
		if ("ADMIN_USER".equals(role.getNom()) && iutilisateur.findAll().stream()
                .anyMatch(utilisateur -> "ADMIN_USER".equals(utilisateur.getRole().getNom()))) {
            throw new RuntimeException("Un compte ADMIN_USER existe déjà.");
	    }
		
		//encoder le mot de passe avant de l'enregistrer
		String encodedPassword=passwordEncoder.encode(password);
		
		//creer un nouvel user avec role specifié
		Utilisateur utilisateur=new Utilisateur();
		utilisateur.setId(id);
		utilisateur.setUsername(username);
		utilisateur.setPassword(encodedPassword);
		utilisateur.setRole(role);
		
		//enregistrer dans la bd
		return iutilisateur.save(utilisateur);
		
	}
	
	@Transactional
	public void changerRoleUtilisateur(String username, String nouveauRole) {
		if ("ADMIN_USER".equals(nouveauRole) && iutilisateur.findAll().stream()
                .anyMatch(utilisateur -> "ADMIN_USER".equals(utilisateur.getRole().getNom()))) {
            throw new RuntimeException("Un compte ADMIN_USER existe déjà.");
        }


	    Utilisateur utilisateur = iutilisateur.findByUsername(username)
	            .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec le nom d'utilisateur : " + username));
	    
	    Role role=irole.findByNom(nouveauRole)
				.orElseThrow(() -> new RuntimeException("Rôle non trouvé"));
	    
	    utilisateur.setRole(role);
	    iutilisateur.save(utilisateur);
	}
	
	@Transactional
	public String reinitialiserMotDePasse(String username) {
	    Utilisateur utilisateur = iutilisateur.findByUsername(username)
	            .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec le nom d'utilisateur : " + username));

	    // Générer un nouveau mot de passe aléatoire
	    String nouveauMotDePasse = genererMotDePasseAleatoire();
	    String encodedPassword = passwordEncoder.encode(nouveauMotDePasse);

	    // Mettre à jour le mot de passe
	    utilisateur.setPassword(encodedPassword);
	    iutilisateur.save(utilisateur);

	    // Retourner le mot de passe en clair pour l'affichage
	    return nouveauMotDePasse;
	}
	
	@Transactional
	public void activerDesactiverCompte(String username, boolean enabled) {
		Utilisateur utilisateur=iutilisateur.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec le nom d'utilisateur : " + username));
		
		utilisateur.setEnabled(enabled);
		iutilisateur.save(utilisateur);
	
	}
	
    public boolean existsByUsername(String username) {
        return iutilisateur.existsByUsername(username);
    }
    
    private String genererMotDePasseAleatoire() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder motDePasse = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) { // Longueur du mot de passe : 10 caractères
            motDePasse.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }  //Cela sera utile lors de la création d'un nouvel utilisateur
        return motDePasse.toString();
    }
    
    public String genererUsernameUnique(String nom, String prenom) {
        String baseUsername = (nom + prenom).toLowerCase().replaceAll("\\s", "");
        String username = baseUsername;
        int compteur = 1;

        while (iutilisateur.existsByUsername(username)) {
            username = baseUsername + compteur;
            compteur++;
        }

        return username;
    }	
}
