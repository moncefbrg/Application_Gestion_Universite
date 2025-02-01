package com.example.demo.services.seuil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.Seuil;
import com.example.demo.repositories.ISeuil;

@Service
public class SeuilServiceImpl implements ISeuilService {

    @Autowired
    private ISeuil seuilRepository;

    @Override
    @Transactional
    public Seuil creerSeuil( Long id,double xNormale, double yRattrapage) {
        
        // Créer un nouveau seuil en utilisant le Builder (si disponible)
        Seuil seuil = Seuil.builder()
        		.id(id)
                .XNormale(xNormale)
                .YRattrapage(yRattrapage)
                .build();

        // Sauvegarder le seuil dans la base de données
        return seuilRepository.save(seuil);
    }

    @Override
    @Transactional
    public Seuil modifierSeuil(Long id, Double nXNormale, Double nYRattrapage) {
        // Récupérer le seuil existant par son ID
        Seuil seuil = seuilRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seuil non trouvé avec l'ID : " + id));

        // Mettre à jour les valeurs du seuil
        if(!(nXNormale==null)) {seuil.setXNormale(nXNormale);}
        if(!(nYRattrapage==null)) {seuil.setYRattrapage(nYRattrapage);}

        // Sauvegarder les modifications
        return seuilRepository.save(seuil);
    }

    @Override
    @Transactional
    public boolean supprimerSeuil(Long id) {
        // Vérifier si le seuil existe
        if (seuilRepository.existsById(id)) {
            // Supprimer le seuil
            seuilRepository.deleteById(id);
            return true;
        } else {
            // Retourner false si le seuil n'existe pas
            return false;
        }
    }
}