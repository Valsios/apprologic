package mg.apprologic.apprologic.controller.article;

import mg.apprologic.apprologic.model.article.Article;
import mg.apprologic.apprologic.model.bons.BonLivraisonMere;
import mg.apprologic.apprologic.model.stock.AnomalieStock;
import mg.apprologic.apprologic.model.stock.StockFille;
import mg.apprologic.apprologic.model.stock.StockMere;
import mg.apprologic.apprologic.model.stock.StockReel;
import mg.apprologic.apprologic.services.article.ArticleService;
import mg.apprologic.apprologic.services.stock.StockFilleService;
import mg.apprologic.apprologic.services.stock.StockMereService;
import mg.apprologic.apprologic.services.stock.StockReelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/stock")
public class InventaireController {

    @Autowired
    ArticleService articleService ;

    @Autowired
    StockReelService stockReelService;

    @Autowired
    StockFilleService stockFilleService;

    @Autowired
    StockMereService stockMereService;

    @GetMapping("/anomalies")
    public String getListeAnomalie(Model model)
    {
        List<AnomalieStock> anomalieStockList = stockMereService.getAnomaliesStock();
        model.addAttribute("anomalie_list",anomalieStockList);
        return "article/AnomalieListe";
    }
    @PostMapping("/inventaire")
    public ResponseEntity<Map<String, String>> uploadFile(
            @RequestParam("quantite") String quantite,
            @RequestParam(value = "articleId", required = false) Integer articleId) {

        try {

            Article article = articleService.getById(articleId);
            Double quantiteReel = Double.parseDouble(quantite);
            LocalDateTime present = LocalDateTime.now();

            StockReel stockReel = new StockReel();
            stockReel.setArticle(article);
            stockReel.setStockReel(quantiteReel);
            stockReel.setDateInventaire(present);

            stockReelService.save(stockReel);
            processStock(stockReel);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Invetaire fait avec l'article "+article.getDesignation()
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Erreur lors de l'inventaire: " + e.getMessage()
            ));
        }
    }

    protected void processStock(StockReel stockReel)
    {
        Double stock_article = stockFilleService.stock_article_date(LocalDateTime.now(),stockReel.getArticle());
        if (stockReel.getStockReel()-stock_article != 0.0)
        {
            StockMere stockMere = new StockMere();
            stockMere.setDescription("Anomalie de stock ");
            stockMere.setDateMouvement(LocalDateTime.now());

            stockMereService.save(stockMere);

            StockFille stockFille = new StockFille();
            stockFille.setStockMere(stockMere);
            stockFille.setSortie(0.0);
            stockFille.setEntree(0.0);
            if (stockReel.getStockReel()<stock_article)
            {
                stockMere.setDescription("Anomalie de stock : deficit.");
                stockFille.setSortie(stock_article-stockReel.getStockReel());
            }
            else
            {
                stockMere.setDescription("Anomalie de stock : excedentaire.");
                stockFille.setEntree(stockReel.getStockReel()-stock_article);
            }
            stockFille.setArticle(stockReel.getArticle());
            stockFille.setUdm(stockFille.getArticle().getUdm());
            stockMereService.save(stockMere);
            stockFilleService.save(stockFille);
        }
    }
}
