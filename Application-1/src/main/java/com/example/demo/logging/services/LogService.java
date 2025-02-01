package com.example.demo.logging.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.demo.logging.entities.Log;
import com.example.demo.logging.repositories.LogRepository;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;
    @Scheduled(fixedRate = 86400000)  // Cette méthode s'exécute toutes les 24 heures (en ms)
    public void deleteOldLogs() {
        LocalDateTime thresholdDate = LocalDateTime.now().minusDays(30);  // Par exemple, supprimer les logs de plus de 30 jours
        logRepository.deleteByLogDateBefore(thresholdDate);  // Supposer que tu as une méthode de suppression dans ton repository
        System.out.println("Logs plus anciens que " + thresholdDate + " supprimés.");
    }

    public void saveLog( String message) {
        Log log = new Log();
        log.setLogDate(LocalDateTime.now());
        log.setMessage(message);
        logRepository.save(log);
    }
}