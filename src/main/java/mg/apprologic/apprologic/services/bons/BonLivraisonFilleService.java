package mg.apprologic.apprologic.services.bons;

import mg.apprologic.apprologic.model.article.Article;
import mg.apprologic.apprologic.model.bons.BonLivraisonFille;
import mg.apprologic.apprologic.model.bons.BonLivraisonMere;
import mg.apprologic.apprologic.model.consommateur.Consommateur;
import mg.apprologic.apprologic.model.fournisseur.Fournisseur;
import mg.apprologic.apprologic.repository.bons.BonLivraisonFilleRepository;
import mg.apprologic.apprologic.repository.consommateur.ConsommateurRepository;
import mg.apprologic.apprologic.services.fournisseur.FournisseurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
public class BonLivraisonFilleService {

    @Autowired
    BonLivraisonFilleRepository bonLivraisonFilleRepository;

    @Autowired
    FournisseurService fournisseurService;

    public List<BonLivraisonFille> getByMere(BonLivraisonMere bonLivraisonMere)
    {
        return bonLivraisonFilleRepository.getBonLivraisonFilleByBonLivraisonMere(bonLivraisonMere);
    }

    public void save(BonLivraisonFille bonLivraisonFille)
    {
        bonLivraisonFilleRepository.save(bonLivraisonFille);
    }

    public void delete(BonLivraisonFille bonLivraisonFille)
    {
        bonLivraisonFilleRepository.delete(bonLivraisonFille);
    }

    public HashMap<String,Double> sommeSatisfaction(List<BonLivraisonFille> bonLivraisonFilleList)
    {
        HashMap<String,Double> toReturn = new HashMap<>();
        double lack = 0.0;
        double quantiteCommande = 0.0;
        double quantiteRecu = 0.0;
        for (BonLivraisonFille bonLivraisonFille : bonLivraisonFilleList)
        {
            quantiteCommande += bonLivraisonFille.getQuantite_demande();
            quantiteRecu += bonLivraisonFille.getQuantite_recu();

            lack += (bonLivraisonFille.getQuantite_recu()/bonLivraisonFille.getQuantite_demande())*100;
            System.out.println("QUANTITE RECU : "+bonLivraisonFille.getQuantite_recu() +" , QUANTITE DEMANDE : "+bonLivraisonFille.getQuantite_demande()+" SO PERCENTAGE =" +lack);
        }
        lack = lack/bonLivraisonFilleList.size();

        toReturn.put("taux",lack);
        toReturn.put("quantiteCommande",quantiteCommande);
        toReturn.put("quantiteRecu",quantiteRecu);
        return toReturn;
    }
    //filtre ARTICLE YEAR
    public HashMap<Fournisseur,Double> tauxSatisfactionLivraison(List<BonLivraisonFille> bonLivraisonFilleList)
    {


        HashMap<Fournisseur,Double> toReturn = new HashMap<>();
        List<Fournisseur> fournisseurList = fournisseurService.getAll();
        for (Fournisseur fournisseur : fournisseurList)
        {
            toReturn.put(fournisseur,0.0);
        }
        for (BonLivraisonFille bonLivraisonFille : bonLivraisonFilleList)
        {
            Fournisseur fournisseur = bonLivraisonFille.getBonLivraisonMere().getFournisseur();
            Double satisfactionBl = (bonLivraisonFille.getQuantite_recu()/bonLivraisonFille.getQuantite_demande())*100;
            Double newValue = (toReturn.get(fournisseur) + satisfactionBl)/2;
            toReturn.put(fournisseur,newValue);
        }
        return toReturn;
    }

    //END OF DAHSBOARD

    @Transactional(readOnly = true)
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
