package mg.apprologic.apprologic.services.local;

import mg.apprologic.apprologic.model.article.Article;
import mg.apprologic.apprologic.model.bons.BonLivraisonFille;
import mg.apprologic.apprologic.model.bons.BordereauFille;
import mg.apprologic.apprologic.model.local.ExistantGisement;
import mg.apprologic.apprologic.model.local.GisementArticle;
import mg.apprologic.apprologic.model.local.GisementStockFille;
import mg.apprologic.apprologic.model.local.Local;
import mg.apprologic.apprologic.repository.local.GisementStockFilleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class GisementStockFilleService
{

    @Autowired
    GisementStockFilleRepository gisementStockFilleRepository;

    @Autowired
    GisementArticleService gisementArticleService;

    //gestion des gisements
    public List<GisementStockFille> allOccupations(Local local,Article article)
    {
        List<Object[]> data = gisementStockFilleRepository.getAllOccupationGisementByLocale(local,article);
        List<GisementStockFille> gisementStockFilleList = new ArrayList<>();
        for (Object[] temp : data)
        {
            GisementStockFille gisementStockFille = new GisementStockFille();
            gisementStockFille.setArticle((Article) temp[0]);
            gisementStockFille.setGisement((ExistantGisement) temp[1]);
            gisementStockFille.setQuantite_in((Double) temp[2]);
            gisementStockFille.setQuantite_out((Double) temp[3]);
            gisementStockFille.setCapaciteMaxUnnitaire((Double) temp[4]);

            System.out.println("Capacité max " +temp[4]);

            gisementStockFilleList.add(gisementStockFille);
        }

        return gisementStockFilleList;
    }

    public List<GisementStockFille> getAllLibre(Local local,Article article)
    {
        List<GisementStockFille> gisementStockFilleList = allOccupations(local,article);
        List<GisementStockFille> toReturn = new ArrayList<>();
        for (GisementStockFille gisementStockFille : gisementStockFilleList)
        {
            if (gisementStockFille.tauxLibre()-100 == 0.0)
            {
                toReturn.add(gisementStockFille);
            }
        }
        return gisementStockFilleList;
    }

    public List<GisementStockFille> getAllOccuped(Local local,Article article)
    {
        List<GisementStockFille> gisementStockFilleList = allOccupations(local, article);
        List<GisementStockFille> toReturn = new ArrayList<>();
        for (GisementStockFille gisementStockFille : gisementStockFilleList)
        {
            if (gisementStockFille.tauxLibre()-100 != 0.0)
            {
                toReturn.add(gisementStockFille);
            }
        }
        return gisementStockFilleList;
    }


    //process for out stock
    public void traiterListeGisementIfEmpty(List<GisementStockFille> gisementStockFilleList,Article article)
    {
        if (gisementStockFilleList.isEmpty())
        {
            List<ExistantGisement> existantGisementList = gisementArticleService.getGisementByArticle(article);
            for (ExistantGisement existantGisement : existantGisementList)
            {
                GisementStockFille gisementStockFille = new GisementStockFille();
                gisementStockFille.setGisement(existantGisement);
                gisementStockFille.setHors_local(0.0);
                gisementStockFille.setQuantite_in(0.0);
                gisementStockFille.setQuantite_out(0.0);
                gisementStockFille.setSortie_hors_local(0.0);
                gisementStockFille.setDateMouvement(LocalDateTime.now());
                gisementStockFille.setArticle(article);

                gisementStockFilleList.add(gisementStockFille);
            }
        }
    }
    public void firstOutBordereauFille(BordereauFille bordereauFille)
    {
        Article article = bordereauFille.getDemandeFille().getArticle();
        List<GisementStockFille> gisementStockFilleList = gisementStockFilleRepository.findGisementStockFilleByArticleOrderByDateMouvementAsc(article);
        traiterListeGisementIfEmpty(gisementStockFilleList,bordereauFille.getDemandeFille().getArticle());
        double quantiteRestante = bordereauFille.getQuantiteSortie();

        for (GisementStockFille gisementStockFille : gisementStockFilleList)
        {

            if (quantiteRestante <= 0) break;
            GisementArticle capacite = gisementArticleService.capaciteGisement(gisementStockFille.getGisement());
            double quantite_in_gisement = gisementStockFille.getQuantite_in()+gisementStockFille.getHors_local()-gisementStockFille.getQuantite_out()-gisementStockFille.getSortie_hors_local();
            if (quantite_in_gisement>0)
            {

                double quantiteSortie = Math.min(quantiteRestante, quantite_in_gisement);
                System.out.println(quantiteSortie);
                if (quantiteSortie>(gisementStockFille.getQuantite_in()-gisementStockFille.getQuantite_out()))
                {
                    double sortie_hors_local = quantiteSortie-(gisementStockFille.getQuantite_in()-gisementStockFille.getQuantite_out());
                    System.out.println(sortie_hors_local);
                    gisementStockFille.setSortie_hors_local(gisementStockFille.getSortie_hors_local()+sortie_hors_local);
                    gisementStockFille.setQuantite_out(gisementStockFille.getQuantite_in());

                }
                else
                {
                    gisementStockFille.setQuantite_out(gisementStockFille.getQuantite_out()+quantiteSortie);
                    gisementStockFille.setSortie_hors_local(0.0);
                }
                gisementStockFilleRepository.save(gisementStockFille);
                quantiteRestante -= quantiteSortie;
            }

        }
    }

   //process for in stock
    public GisementStockFille occupationGisement(ExistantGisement existantGisement)
    {
        List<Object[]> getOccupationGisement = gisementStockFilleRepository.getOccupationGisement(existantGisement);
        Object[] data = getOccupationGisement.get(0);
        GisementStockFille gisementStockFille = new GisementStockFille();
        gisementStockFille.setGisement((ExistantGisement) data[0]);
        gisementStockFille.setQuantite_in((Double) data[1]);
        gisementStockFille.setQuantite_out((Double) data[2]);
        return gisementStockFille;
    }

    public List<GisementStockFille> getAllOccupation(List<ExistantGisement> existantGisementList)
    {
        List<GisementStockFille> gisementStockFilleList = new ArrayList<>();
        for (ExistantGisement existantGisement : existantGisementList)
        {
            gisementStockFilleList.add(occupationGisement(existantGisement));
        }
        return gisementStockFilleList.stream().sorted(Comparator.comparingDouble(g -> {
                    GisementArticle capacite = gisementArticleService.capaciteGisement(g.getGisement());
                    return capacite.getCapaciteMaxUnitaire() - (g.getQuantite_in() - g.getQuantite_out());
                }))
                .collect(Collectors.toList());
    }
    public void firstInBonLivraison(BonLivraisonFille bonLivraisonFille) throws Exception{
        // 1. Vérifications initiales
        if (bonLivraisonFille == null || bonLivraisonFille.getQuantite_recu() <= 0) {
            throw new IllegalArgumentException("Bon de livraison invalide");
        }

        List<ExistantGisement> gisements = gisementArticleService.getGisementByArticle(bonLivraisonFille.getArticle());
        if (gisements.isEmpty()) {
            throw new IllegalStateException("Aucun gisement disponible pour cet article");
        }

        List<GisementStockFille> occupations = getAllOccupation(gisements);
        double quantiteRestante = bonLivraisonFille.getQuantite_recu();

        GisementStockFille last = occupations.get(0);
        // 2. Parcours des gisements
        for (GisementStockFille gisement : occupations) {
            if (quantiteRestante <= 0) break;

            GisementArticle capacite = gisementArticleService.capaciteGisement(gisement.getGisement());
            double placeLibre = (capacite.getCapaciteMaxUnitaire()) - (gisement.getQuantite_in() - (gisement.getQuantite_out()));

            if (placeLibre > 0) {
                double quantiteAjoutee = Math.min(quantiteRestante, placeLibre);

                GisementStockFille nouveauStock = new GisementStockFille();
                nouveauStock.setQuantite_in(quantiteAjoutee);
                nouveauStock.setQuantite_out(0.0);
                nouveauStock.setGisement(gisement.getGisement());
                nouveauStock.setArticle(bonLivraisonFille.getArticle());
                nouveauStock.setDateMouvement(bonLivraisonFille.getBonLivraisonMere().getDateReception());
                nouveauStock.setHors_local(0.0);
                nouveauStock.setSortie_hors_local(0.0);

                save(nouveauStock);
                last = nouveauStock;
                quantiteRestante -= quantiteAjoutee;
            }
        }

        // 3. Gestion du surplus
        if (quantiteRestante > 0) {
            last.setHors_local(quantiteRestante);
            save(last);
            throw new Exception(String.format(
                    "%.2f unités de "+bonLivraisonFille.getArticle().getDesignation()+" sont hors local.",
                    quantiteRestante
            ));
        }
    }
    public void save(GisementStockFille gisementStockFille)
    {
        gisementStockFilleRepository.save(gisementStockFille);
    }
}
