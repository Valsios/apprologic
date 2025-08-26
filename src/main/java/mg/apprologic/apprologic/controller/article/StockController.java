package mg.apprologic.apprologic.controller.article;

import mg.apprologic.apprologic.model.article.Article;
import mg.apprologic.apprologic.model.bons.BonLivraisonFille;
import mg.apprologic.apprologic.model.bons.BordereauFille;
import mg.apprologic.apprologic.model.consommateur.Consommateur;
import mg.apprologic.apprologic.model.fournisseur.Fournisseur;
import mg.apprologic.apprologic.model.stock.StockFille;
import mg.apprologic.apprologic.services.article.ArticleService;
import mg.apprologic.apprologic.services.bons.BonLivraisonFilleService;
import mg.apprologic.apprologic.services.bons.BordereauFilleService;
import mg.apprologic.apprologic.services.stock.StockFilleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/article")
public class StockController {

    @Autowired
    StockFilleService stockFilleService;

    @Autowired
    BordereauFilleService bordereauFilleService;

    @Autowired
    BonLivraisonFilleService bonLivraisonFilleService;

    @Autowired
    ArticleService articleService;

    @GetMapping("/dashboard")
    public String dashboardArticle(Integer idArticle,Model model)
    {
        Article article = articleService.getById(idArticle);

        Integer currentYear = LocalDateTime.now().getYear();
        //liste bordereau fille par year article
        List<BordereauFille> bordereauFilleList = bordereauFilleService.getBordereauFilleByArticleAndYear(article,currentYear);
        HashMap<String,Double> tauxSatisfactionDemande = bordereauFilleService.tauxSatisfactionDemande(bordereauFilleList);
        HashMap<Consommateur,Double> departementPlusConsommateur = bordereauFilleService.departementPlusConsommateur(bordereauFilleList);

        //bon bonLivraison par year article
        List<BonLivraisonFille> bonLivraisonFilleList = bonLivraisonFilleService.getBonLivraisonFilleByArticleAndYear(article,currentYear);
        HashMap<Fournisseur,Double> tauxSatisfactionLivraison = bonLivraisonFilleService.tauxSatisfactionLivraison(bonLivraisonFilleList);

        //evolution Mensuel
        HashMap<Integer,Double> evolutionMensuel = stockFilleService.evolutionMensuel(currentYear,article);

        //historique evolution
        List<StockFille> stockFilleList = stockFilleService.getStockFilleByArticleAndYear(article,currentYear);

        model.addAttribute("tauxSatisfactionDemande",tauxSatisfactionDemande);
        model.addAttribute("departementPlusConsommateur",departementPlusConsommateur);
        model.addAttribute("tauxSatisfactionLivraison",tauxSatisfactionLivraison);
        model.addAttribute("evolutionMensuel",evolutionMensuel);
        model.addAttribute("historique",stockFilleList);

        return "article/DashboardArticle";
    }
    @GetMapping("/stock")
    public String getStockListe(Model model)
    {
        model.addAttribute("liste_article",stockFilleService.stock_date(null,null));
        return "article/StockArticle";
    }

    @PostMapping("/stockSearch")
    @ResponseBody
    public List<StockFille> search(@RequestParam(required = false) String searchTerm,
                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime searchDate){
        // Si le critère de recherche est vide, passer null pour désactiver le filtrage
        searchTerm = StringUtils.hasText(searchTerm) ? searchTerm : null;

        return stockFilleService.stock_date(searchDate, searchTerm);
    }


}
