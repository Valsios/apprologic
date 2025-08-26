package mg.apprologic.apprologic.services.bons;

import mg.apprologic.apprologic.model.article.Article;
import mg.apprologic.apprologic.model.bons.BonLivraisonFille;
import mg.apprologic.apprologic.model.consommateur.Consommateur;
import mg.apprologic.apprologic.model.fournisseur.Fournisseur;
import mg.apprologic.apprologic.repository.bons.BonLivraisonFilleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
public class BonLivraisonFilleService {

    @Autowired
    BonLivraisonFilleRepository bonLivraisonFilleRepository;

    public void save(BonLivraisonFille bonLivraisonFille)
    {
        bonLivraisonFilleRepository.save(bonLivraisonFille);
    }

    public void delete(BonLivraisonFille bonLivraisonFille)
    {
        bonLivraisonFilleRepository.delete(bonLivraisonFille);
    }


    //filtre ARTICLE YEAR
    public HashMap<Fournisseur,Double> tauxSatisfactionLivraison(List<BonLivraisonFille> bonLivraisonFilleList)
    {
        HashMap<Fournisseur,Double> toReturn = new HashMap<>();
        for (BonLivraisonFille bonLivraisonFille : bonLivraisonFilleList)
        {
            Fournisseur fournisseur = bonLivraisonFille.getBonLivraisonMere().getFournisseur();
            Double satisfactionBl = (bonLivraisonFille.getQuantite_demande()/bonLivraisonFille.getQuantite_recu())*100;
            if (toReturn.containsKey(fournisseur))
            {

                Double newValue = (toReturn.get(fournisseur) + satisfactionBl)/2;
                toReturn.put(fournisseur,newValue);
            }
            else
            {
                toReturn.put(fournisseur,satisfactionBl);
            }
        }
        return toReturn;
    }

    //END OF DAHSBOARD

    public List<BonLivraisonFille> getBonLivraisonFilleByArticleAndYear(Article article,Integer year)
    {
        return bonLivraisonFilleRepository.getBonLivraisonFilleByArticleAndYear(article,year);
    }
    @Transactional(readOnly = true)
    public List<BonLivraisonFille> getLivraisonByArticle(Article article)
    {
        return bonLivraisonFilleRepository.getBonLivraisonFilleByArticle(article);
    }

}
