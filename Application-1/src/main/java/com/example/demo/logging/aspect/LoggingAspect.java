package com.example.demo.logging.aspect;

import com.example.demo.entities.Etudiant;
import com.example.demo.logging.services.HistoriqueEtudiantService;
import com.example.demo.logging.services.LogService;
import com.example.demo.repositories.IEtudiant;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Autowired
    private LogService logService;
    @Autowired
    private HistoriqueEtudiantService historiqueEtudiantService;
    @Autowired
    private IEtudiant iEtudiant;
    private Object[] old;

    @After("execution(* com.example.demo.services.*.*.*(..)) || execution(* com.example.demo.security.services.Utilisateur.UtilisateurService.*(..))")    
    public void logAfter(JoinPoint joinPoint) {
    	// Récupère la signature de la méthode
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName(); // Nom de la méthode
        //Class<?>[] parameterTypes = signature.getMethod().getParameterTypes(); // Types des paramètres
        Object[] args = joinPoint.getArgs(); // Valeurs des paramètres
        String message = methodName+ Arrays.toString(args);
        logService.saveLog(message);
    }
    @Before("execution(* com.example.demo.services.Etudiant.EtudiantServiceimpl.modifierEtudiant(..))")
    public void avantModification(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Long id = (Long) args[0];
        if (iEtudiant.findById(id).isPresent()) {
            Etudiant ancienEtudiant = iEtudiant.findById(id).get();
           old = new Object[] {
                    ancienEtudiant.getId(),
                    ancienEtudiant.getCne(),
                    ancienEtudiant.getNom(),
                    ancienEtudiant.getPrenom(),
                    ancienEtudiant.getNiveau().getAlias()
                };
        }
    }

    @After("execution(* com.example.demo.services.Etudiant.EtudiantServiceimpl.modifierEtudiant(..))")    
    public void historiqueEtudianAfter(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        
        String oldvalues = Arrays.toString(old);
        String newValues = Arrays.toString(args);
        historiqueEtudiantService.saveHistoriqueEtudiant(oldvalues, newValues);
    }
    
}