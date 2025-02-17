package com.example.demo.services.formule;
//log done
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.Formule;
import com.example.demo.entities.Niveau;
import com.example.demo.repositories.IFormule;
import com.example.demo.repositories.INiveau;
@Service
public class FormuleServiceimpl implements IFormuleService{
	@Autowired
	private IFormule iFormule;
	@Autowired
	private INiveau iNiveau;
	
	private static final Logger logger=LoggerFactory.getLogger(FormuleServiceimpl.class);

	@Override @Transactional
	public Formule creerFormule(Long id, String nom, String expression, List<String> param,Niveau niveau) {
		// Vérifier si une Formule avec le même ID existe déjà
        if (iFormule.existsById(id)) {
            throw new RuntimeException("Une formule avec l'ID " + id + " existe déjà.");
        }
        
        if (iNiveau.findById(niveau.getId()).isEmpty()) {
            throw new RuntimeException("Le niveau avec le nom " + niveau.getNom() + " existe pas.");
        }

        // Créer une nouvelle formule en utilisant le Builder (si disponible)
        Formule formule = Formule.builder()
                .id(id)
                .expression(expression)
                .nom(nom)
                .parametres(param)
                .niveau(niveau)
                .build();
        logger.info("Creation de formule : "+id+","+expression+",pour niv : "+niveau.getAlias());

        // Sauvegarder la Formule dans la base de données
        return iFormule.save(formule);
    }
	

	@Override
	public Formule modifierFormule(Long id, String nouveauNom, String nouvelleExpression, List<String> nParam) {
		// Récupérer la formule existant par son ID
		Formule formule = iFormule.findById(id)
                .orElseThrow(() -> new RuntimeException("La formule non trouvé avec l'ID : " + id));

        // Mettre à jour les valeurs de la formule
        if(!(nouveauNom==null)) {formule.setNom(nouveauNom);}
        if(!(nouvelleExpression==null)) {formule.setExpression(nouvelleExpression);}
        if(!(nParam==null)) {formule.setParametres(nParam);}
        
        logger.info("Modification de formule : "+id+"avec "+nouvelleExpression);

        // Sauvegarder les modifications
        return iFormule.save(formule);

	}

	@Override @Transactional
	public boolean supprimerFormule(Long id) {
		// Vérifier si la Formule existe
        if (iFormule.existsById(id)) {
            // Supprimer la Formule
        	iFormule.deleteById(id);
            logger.info("Supression de formule : "+id);

            return true;
        } else {
            // Retourner false si la formule n'existe pas
            return false;
        }
    }


	@Override
	public List<Formule> getAllFormules() {
		return iFormule.findAll();
	}


	@Override
	public List<Formule> getFormulesByIds(List<Long> ids) {
		return iFormule.findAllById(ids);
	}

}
