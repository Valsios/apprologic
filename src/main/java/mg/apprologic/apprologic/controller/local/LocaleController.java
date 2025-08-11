package mg.apprologic.apprologic.controller.local;

import mg.apprologic.apprologic.model.article.Article;
import mg.apprologic.apprologic.model.local.ExistantGisement;
import mg.apprologic.apprologic.model.local.GisementArticle;
import mg.apprologic.apprologic.model.local.GisementStockFille;
import mg.apprologic.apprologic.model.local.Local;
import mg.apprologic.apprologic.model.stock.StockReel;
import mg.apprologic.apprologic.services.article.ArticleService;
import mg.apprologic.apprologic.services.local.ExistantGisementService;
import mg.apprologic.apprologic.services.local.GisementArticleService;
import mg.apprologic.apprologic.services.local.GisementStockFilleService;
import mg.apprologic.apprologic.services.local.LocalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/local")
public class LocaleController {


    @Autowired
    GisementStockFilleService gisementStockFilleService;

    @Autowired
    ExistantGisementService existantGisementService;

    @Autowired
    GisementArticleService gisementArticleService;

    @Autowired
    LocalService localService;

    @Autowired
    ArticleService articleService;

    @PostMapping("/assign")
    public ResponseEntity<Map<String, String>> assign(
            @RequestParam("capacite") String capacite,
            @RequestParam(value = "articleAssignId", required = false) Integer articleAssignId,
            @RequestParam(value = "gisementId", required = false) Integer gisementId) {

        try {

            Article article = articleService.getById(articleAssignId);
            ExistantGisement existantGisement = existantGisementService.getById(gisementId);
            GisementArticle gisementArticle = gisementArticleService.getBYGisement(existantGisement);
            gisementArticle.setCapaciteMaxUnitaire(Double.parseDouble(capacite));
            gisementArticle.setArticle(article);
            gisementArticleService.save(gisementArticle);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Assignement fait pour "+existantGisement.getLocal()+existantGisement.getTrave()+existantGisement.getAlveole()+existantGisement.getEtagere()+existantGisement.getBac()+"."
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Erreur lors de l'assignement: " + e.getMessage()
            ));
        }
    }
    @GetMapping("/occupations")
    public String getAllOccupation(Model model)
    {
        model.addAttribute("allLocals",localService.getAllLocal());
        model.addAttribute("local_liste",gisementStockFilleService.allOccupations(null,null));
        return "local/LocalListe";
    }

    @GetMapping("/filtre")
    public ResponseEntity<List<GisementStockFille>> getOccupation(
            @RequestParam(required = false) Integer localId,
            @RequestParam(required = false) Integer articleId) {

        Local local = localService.getById(localId);
        Article article = articleService.getById(articleId);

        List<GisementStockFille> result = gisementStockFilleService.allOccupations(local, article);
        return ResponseEntity.ok(result);
    }
}
