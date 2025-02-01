package com.example.demo.services.seuil;

//log done
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.Seuil;
import com.example.demo.repositories.ISeuil;

@Service
public class SeuilServiceImpl implements ISeuilService {

    @Autowired
    private ISeuil seuilRepository;
    
	private static final Logger logger=LoggerFactory.getLogger(SeuilServiceImpl.class);

    @Override
    @Transactional
    public Seuil creerSeuil( Long id,double xNormale, double yRattrapage) {
        
        // Créer un nouveau seuil en utilisant le Builder (si disponible)
        Seuil seuil = Seuil.builder()
        		.id(id)
                .XNormale(xNormale)
                .YRattrapage(yRattrapage)
                .build();
        logger.info("Creation seuil avec id :"+id+"X: "+xNormale+" Y:"+yRattrapage);
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
        logger.info("Modification seuil avec id :"+id+"X: "+nXNormale+" Y:"+nYRattrapage);

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
            logger.info("Supression seuil avec id :"+id);

            return true;
        } else {
            // Retourner false si le seuil n'existe pas
            return false;
        }
    }

	@Override
	public List<Seuil> getAllSeuils() {
		return seuilRepository.findAll();
	}

	@Override
	public Seuil getSeuilById(Long id) {
		return seuilRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Seuil non trouvé avec l'ID :" + id));
	}
}