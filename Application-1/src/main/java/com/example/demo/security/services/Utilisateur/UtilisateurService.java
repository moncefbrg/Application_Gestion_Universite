package com.example.demo.security.services.Utilisateur;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.security.repositories.IUtilisateur;

import org.springframework.transaction.annotation.Transactional;

import com.example.demo.security.entities.Utilisateur;

@Service
public class UtilisateurService implements UserDetailsService {
	@Autowired
	private IUtilisateur iutilisateur;
	
	@Autowired
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
				utilisateur.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority("ROLE_" + role))
				.collect(Collectors.toList()));
	}
	
	@Transactional
	public Utilisateur inscrireUtilisateur(String username, String password, Set<String> roles) {
		//on verifie si l'utilisateur existe deja
		if(iutilisateur.findByUsername(username).isPresent()) {
			throw new RuntimeException("Ce nom d'utilisateur est déjà pris !");
		}
		
		//encoder le mot de passe avant de l'enregistrer
		String encodedPassword=passwordEncoder.encode(password);
		
		//creer un nouvel user avec role specifié
		Utilisateur utilisateur=new Utilisateur();
		utilisateur.setUsername(username);
		utilisateur.setPassword(encodedPassword);
		utilisateur.setRoles(new HashSet<>(roles));
		
		//enregistrer dans la bd
		return iutilisateur.save(utilisateur);
		
	}
	
	@Transactional
	public void changerRoleUtilisateur(String username, Set<String> nouveauxRoles) {
		Utilisateur utilisateur = iutilisateur.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec le nom d'utilisateur : " + username));
		
		utilisateur.setRoles(new HashSet<>(nouveauxRoles));
		iutilisateur.save(utilisateur);
	}
	
	@Transactional
	public void reinitialiserMotDePasse(String username, String nouveauMotDePasse) {
		Utilisateur utilisateur=iutilisateur.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec le nom d'utilisateur : " + username));
		
		String encodedPassword=passwordEncoder.encode(nouveauMotDePasse);
		utilisateur.setPassword(encodedPassword);
		iutilisateur.save(utilisateur);
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

	

}
