package mg.apprologic.apprologic.services.importUtil;


import mg.apprologic.apprologic.model.article.Article;
import mg.apprologic.apprologic.model.article.Devise;
import mg.apprologic.apprologic.model.article.Udm;
import mg.apprologic.apprologic.model.bons.*;
import mg.apprologic.apprologic.model.consommateur.Consommateur;
import mg.apprologic.apprologic.model.fournisseur.Fournisseur;
import mg.apprologic.apprologic.model.local.ExistantGisement;
import mg.apprologic.apprologic.model.local.GisementArticle;
import mg.apprologic.apprologic.model.local.GisementStockFille;
import mg.apprologic.apprologic.model.local.Local;
import mg.apprologic.apprologic.model.stock.StockFille;
import mg.apprologic.apprologic.model.stock.StockMere;
import mg.apprologic.apprologic.model.stock.StockReel;
import mg.apprologic.apprologic.services.article.*;
import mg.apprologic.apprologic.services.bons.*;
import mg.apprologic.apprologic.services.consommateur.AdresseService;
import mg.apprologic.apprologic.services.consommateur.ConsommateurService;
import mg.apprologic.apprologic.services.consommateur.TransportService;
import mg.apprologic.apprologic.services.fournisseur.FournisseurService;
import mg.apprologic.apprologic.services.local.ExistantGisementService;
import mg.apprologic.apprologic.services.local.GisementArticleService;
import mg.apprologic.apprologic.services.local.GisementStockFilleService;
import mg.apprologic.apprologic.services.local.LocalService;
import mg.apprologic.apprologic.services.stock.StockFilleService;
import mg.apprologic.apprologic.services.stock.StockMereService;
import mg.apprologic.apprologic.services.stock.StockReelService;
import mg.apprologic.apprologic.util.ImportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ImportService {


    @Autowired
    ExistantGisementService existantGisementService;


    @Autowired
    ArticleService articleService;


    @Autowired
    LocalService localService;


    @Autowired
    GisementArticleService gisementArticleService;


    @Autowired
    UdmService udmService;


    @Autowired
    FamilleService familleService;


    @Autowired
    CentreBudgetaireService centreBudgetaireService;


    @Autowired
    StockReelService stockReelService;


    @Autowired
    GisementStockFilleService gisementStockFilleService;


    @Autowired
    StockFilleService stockFilleService;


    @Autowired
    StockMereService stockMereService;


    @Autowired
    DeviseService deviseService;


    @Autowired
    FournisseurService fournisseurService;

    @Autowired
    BonLivraisonFilleService bonLivraisonFilleService;

    @Autowired
    BonLivraisonMereService bonLivraisonMereService;

    @Autowired
    BordereauMereService bordereauMereService;

    @Autowired
    BordereauFilleService bordereauFilleService;

    @Autowired
    DemandeMereService demandeMereService;

    @Autowired
    DemandeFilleService demandeFilleService;


    @Autowired
    AdresseService adresseService;


    @Autowired
    ConsommateurService consommateurService;


    @Autowired
    TransportService transportService;



    public void saveLine(String[] line) throws Exception
    {

        Devise devise = deviseService.getById(1);
        Fournisseur fournisseur = fournisseurService.getById(1);
        Consommateur consommateur = consommateurService.getById(1);
        ImportUtil importUtil = new ImportUtil(line);
        String codeArticle =importUtil.getPGI();
        String designation =importUtil.getDesignation();
        String localString = importUtil.getLocal();
        String trave = importUtil.getTrave();
        String alveole = importUtil.getAlveole();
        String etagere = importUtil.getEtagere();
        String bac = importUtil.getBac();
        String udmString = importUtil.getUdm();
        String stockInitial = importUtil.getStockInitiale();
        String entree = importUtil.getEntree() ;
        String sortie = importUtil.getSortie();


        //UDM
        Udm udm = udmService.getByAcronyme(udmString);
        if (udm == null)
        {
            Udm newUdm = new Udm();
            newUdm.setAcronyme(udmString);
            newUdm.setDescription(udmString);
            udmService.save(newUdm);
            udm = newUdm;
        }
        List<Article> articleList = articleService.getByDesignation(designation);

        Article article = new Article();
        if(articleList.size() != 0)
        {
            article = articleList.get(0);
        }
        else
        {
            article.setCodeArticle(codeArticle);
            article.setDesignation(designation);
            article.setUdm(udm);
            article.setFamille(familleService.getById(1));
            article.setCentreBudgetaire(centreBudgetaireService.getById(1));

            article.setSeuilMin((int)(Integer.parseInt(stockInitial)/2) + 20.0);

            articleService.save(article);
        }
        Local local = localService.getByDesignation(localString);
        if (local == null)
        {
            Local newLocal = new Local();
            newLocal.setDesignation(localString);
            localService.save(newLocal);
            local = newLocal;
        }

        System.out.println("LIGNE d'article : "+designation);

        ExistantGisement existantGisement = existantGisementService.getByAll(local,trave,alveole,etagere,bac);
        if (existantGisement == null)
        {
            ExistantGisement newGisement = new ExistantGisement();
            newGisement.setLocal(local);
            newGisement.setTrave(trave != null && !trave.isEmpty() ? Integer.parseInt(trave) : 0);
            newGisement.setAlveole(alveole != null && !alveole.isEmpty() ? alveole : "T");
            newGisement.setEtagere(etagere != null && !etagere.isEmpty() ? Integer.parseInt(etagere) : 0);
            newGisement.setBac(bac != null && !bac.isEmpty() ? Integer.parseInt(bac) : 0);
            existantGisementService.save(newGisement);
            existantGisement = newGisement;
        }

        GisementArticle gisementArticle = gisementArticleService.getBYGisement(existantGisement);
        if (gisementArticle == null)
        {
            gisementArticle = new GisementArticle();
            gisementArticle.setGisement(existantGisement);
            gisementArticle.setArticle(article);
            gisementArticle.setCapaciteMaxUnitaire((int)(Integer.parseInt(stockInitial)*2)+100.0);
            gisementArticleService.save(gisementArticle);
        }
        else
        {
            gisementArticle.setCapaciteMaxUnitaire(gisementArticle.getCapaciteMaxUnitaire()+(int)(Integer.parseInt(stockInitial)*2)+100);
            gisementArticleService.save(gisementArticle);
        }


        if (Integer.parseInt(stockInitial) != 0)
        {


            LocalDateTime present = LocalDateTime.now();
            Integer quantite = Integer.parseInt(stockInitial);

            BonLivraisonMere bonLivraisonMere = new BonLivraisonMere();
            bonLivraisonMere.setDateReception(present);
            bonLivraisonMere.setDevise(devise);
            bonLivraisonMere.setFournisseur(fournisseur);
            bonLivraisonMere.setDescription("IMPORT DEPUIS EXCEL");
            bonLivraisonMereService.save(bonLivraisonMere);

            BonLivraisonFille bonLivraisonFille = new BonLivraisonFille();
            bonLivraisonFille.setBonLivraisonMere(bonLivraisonMere);
            bonLivraisonFille.setQuantite_demande(quantite.doubleValue());
            bonLivraisonFille.setQuantite_recu(quantite.doubleValue());
            bonLivraisonFille.setArticle(article);
            bonLivraisonFille.setPrixUnitaire(1000.0);
            bonLivraisonFilleService.save(bonLivraisonFille);

            StockMere stockMere = new StockMere();
            stockMere.setDateMouvement(present);
            stockMere.setDescription("IMPORT DEPUIS EXCEL");
            stockMere.setBonLivraisonMere(bonLivraisonMere);
            stockMereService.save(stockMere);

            StockFille stockFille = new StockFille();
            stockFille.setStockMere(stockMere);
            stockFille.setArticle(article);
            stockFille.setEntree(quantite.doubleValue());
            stockFille.setUdm(article.getUdm());
            stockFille.setSortie(0.0);
            stockFilleService.save(stockFille);

            GisementStockFille gisementStockFille = gisementStockFilleService.occupationGisement(existantGisement);

            if (gisementStockFille.getArticle() == null)
            {
                gisementStockFille = new GisementStockFille();
                gisementStockFille.setGisement(existantGisement);
                gisementStockFille.setArticle(article);
                gisementStockFille.setQuantite_in(quantite.doubleValue());
                gisementStockFille.setQuantite_out(0.0);
                gisementStockFille.setHors_local(0.0);
                gisementStockFille.setSortie_hors_local(0.0);
                gisementStockFille.setDateMouvement(present);
                gisementStockFilleService.save(gisementStockFille);

            }
            else
            {
                gisementStockFille.setQuantite_in(gisementStockFille.getQuantite_in()+quantite);
                gisementStockFilleService.save(gisementStockFille);
            }

        }

        if (entree != null && !entree.isEmpty())
        {
            LocalDateTime present = LocalDateTime.now();
            Integer quantite = Integer.parseInt(entree);

            BonLivraisonMere bonLivraisonMere = new BonLivraisonMere();
            bonLivraisonMere.setDateReception(present);
            bonLivraisonMere.setDevise(devise);
            bonLivraisonMere.setFournisseur(fournisseur);
            bonLivraisonMere.setDescription("IMPORT DEPUIS EXCEL");
            bonLivraisonMereService.save(bonLivraisonMere);

            BonLivraisonFille bonLivraisonFille = new BonLivraisonFille();
            bonLivraisonFille.setBonLivraisonMere(bonLivraisonMere);
            bonLivraisonFille.setQuantite_demande(quantite.doubleValue());
            bonLivraisonFille.setQuantite_recu(quantite.doubleValue());
            bonLivraisonFille.setArticle(article);
            bonLivraisonFille.setPrixUnitaire(1000.0);
            bonLivraisonFilleService.save(bonLivraisonFille);

            StockMere stockMere = new StockMere();
            stockMere.setDateMouvement(present);
            stockMere.setDescription("IMPORT DEPUIS EXCEL");
            stockMere.setBonLivraisonMere(bonLivraisonMere);
            stockMereService.save(stockMere);

            StockFille stockFille = new StockFille();
            stockFille.setStockMere(stockMere);
            stockFille.setArticle(article);
            stockFille.setEntree(quantite.doubleValue());
            stockFille.setUdm(article.getUdm());
            stockFille.setSortie(0.0);
            stockFilleService.save(stockFille);
            GisementStockFille gisementStockFille = gisementStockFilleService.occupationGisement(existantGisement);
            if (gisementStockFille.getArticle() == null)
            {
                gisementStockFille = new GisementStockFille();
                gisementStockFille.setGisement(existantGisement);
                gisementStockFille.setArticle(article);
                gisementStockFille.setQuantite_in(quantite.doubleValue());
                gisementStockFille.setQuantite_out(0.0);
                gisementStockFille.setHors_local(0.0);
                gisementStockFille.setSortie_hors_local(0.0);
                gisementStockFille.setDateMouvement(present);
                gisementStockFilleService.save(gisementStockFille);

            }
            else

            {
                gisementStockFille.setQuantite_in(gisementStockFille.getQuantite_in()+quantite);
                gisementStockFilleService.save(gisementStockFille);
            }


        }

        if (sortie != null && !sortie.isEmpty())
        {
            LocalDateTime present = LocalDateTime.now();
            Integer quantite = Integer.parseInt(sortie);

            DemandeMere demandeMere = new DemandeMere();
            demandeMere.setDateDemande(present);
            demandeMere.setDateSortie(present);
            demandeMere.setAdresse(adresseService.getById(1));
            demandeMere.setConsommateur(consommateur);
            demandeMere.setCodePgi("PGI");
            demandeMereService.save(demandeMere);

            DemandeFille demandeFille = new DemandeFille();
            demandeFille.setDemandeMere(demandeMere);
            demandeFille.setQuantite(quantite.doubleValue());
            demandeFille.setUdm(article.getUdm());
            demandeFille.setArticle(article);
            demandeFilleService.save(demandeFille);

            BordereauMere bordereauMere = new BordereauMere();
            bordereauMere.setDateBordereau(present);
            bordereauMere.setDemandeMere(demandeMere);
            bordereauMere.setDescription("IMPORT DEPUIS EXCEL");
            bordereauMere.setDevise(devise);
            bordereauMere.setTransport(transportService.getById(1));
            bordereauMereService.save(bordereauMere);

            BordereauFille bordereauFille = new BordereauFille();
            bordereauFille.setBordereauMere(bordereauMere);
            bordereauFille.setDemandeFille(demandeFille);
            bordereauFille.setQuantiteSortie(quantite.doubleValue());
            bordereauFille.setPrixUnitaire(1000.0);
            bordereauFilleService.save(bordereauFille);

            StockMere stockMere = new StockMere();
            stockMere.setDateMouvement(present);
            stockMere.setDescription("IMPORT DEPUIS EXCEL");
            stockMere.setDemandeMere(demandeMere);
            stockMereService.save(stockMere);

            StockFille stockFille = new StockFille();
            stockFille.setStockMere(stockMere);
            stockFille.setArticle(article);
            stockFille.setEntree(0.0);
            stockFille.setUdm(article.getUdm());
            stockFille.setSortie(quantite.doubleValue());
            stockFilleService.save(stockFille);

            GisementStockFille gisementStockFille = gisementStockFilleService.occupationGisement(existantGisement);

            if (gisementStockFille.getArticle() == null)
            {
                gisementStockFille = new GisementStockFille();
                gisementStockFille.setGisement(existantGisement);
                gisementStockFille.setArticle(article);
                gisementStockFille.setQuantite_in(0.0);
                gisementStockFille.setQuantite_out(quantite.doubleValue());
                gisementStockFille.setHors_local(0.0);
                gisementStockFille.setSortie_hors_local(0.0);
                gisementStockFille.setDateMouvement(present);
                gisementStockFilleService.save(gisementStockFille);
            }
            else
            {
                gisementStockFille.setQuantite_out(gisementStockFille.getQuantite_out() + quantite.doubleValue());
                gisementStockFilleService.save(gisementStockFille);
            }








        }


    }


}
