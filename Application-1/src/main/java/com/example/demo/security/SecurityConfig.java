package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.security.services.Utilisateur.UtilisateurService;



@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private UtilisateurService utilisateurService;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	        .authorizeHttpRequests((requests) -> requests
	            .requestMatchers("/", "/login").permitAll() // Pages accessibles sans connexion
	            .requestMatchers("/**", "/niveaux/**").hasRole("ADMIN_USER") // Protéger les routes admin
	            .requestMatchers("/etudiants/**").hasRole("ADMIN_NOTE")
	            .requestMatchers("/niveaux/**", "/modules/**").hasRole("ADMIN_SP")
	            .anyRequest().authenticated() // Tout le reste nécessite une authentification
	        )
	        .formLogin((form) -> form
	            .loginPage("/login")  // Page de connexion personnalisée
	            .defaultSuccessUrl("/acceuil", true)   // Redirection après connexion réussie
	            .failureUrl("/login?error=true") // Redirection en cas d'échec de connexion
	            .permitAll()
	        )
	        .logout((logout) -> logout
	            .logoutSuccessUrl("/")
	            .permitAll()
	        )
	        .exceptionHandling(ex -> ex
	            .accessDeniedPage("/403") // Page d'erreur, accès refusé
	        );

	    return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	/*
	 * @Bean public AuthenticationManager
	 * authenticationManager(AuthenticationConfiguration authConfig) throws
	 * Exception { return authConfig.getAuthenticationManager(); }
	 */
	
	/*
	 * @Bean public PasswordEncoder passwordEncoder() { //pour encoder les mdp avant
	 * de les stocker en base return new BCryptPasswordEncoder(); }
	 */
	
	/*
	 * @Bean public AuthenticationProvider authenticationProvider() {
	 * DaoAuthenticationProvider authProvider=new DaoAuthenticationProvider();
	 * //DaoAuthenticationP permet d'utiliser notre BD pour l'authentification
	 * authProvider.setUserDetailsService(utilisateurService); //il dit a spring d
	 * utiliser UtilisateurService et PasswordEncoder
	 * authProvider.setPasswordEncoder(passwordEncoder()); return authProvider; }
	 */

    /*@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/", "login").permitAll() // Autoriser l'accès à la page d'accueil sans authentification
                .requestMatchers("/admin/**").hasRole("ADMIN") // Protéger les URLs /admin/** pour les ADMIN
                .anyRequest().authenticated() // Toutes les autres URLs nécessitent une authentification
            )
            .formLogin((form) -> form
                .loginPage("/login") // Spécifier la page de login personnalisée
                .defaultSuccessUrl("/acceuil", true)
                .permitAll()
            )
            .logout((logout) -> logout
            		.logoutSuccessUrl("/")
            		.permitAll());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Créer un utilisateur ADMIN
        UserDetails admin = User.withDefaultPasswordEncoder()
            .username("admin") // Nom d'utilisateur
            .password("admin123") // Mot de passe
            .roles("ADMIN") // Rôle ADMIN
            .build();

        // Créer un utilisateur USER (optionnel)
        UserDetails user = User.withDefaultPasswordEncoder()
            .username("user")
            .password("user123")
            .roles("USER")
            .build();

        // Retourner les utilisateurs configurés
        return new InMemoryUserDetailsManager(admin, user);
    }*/
}