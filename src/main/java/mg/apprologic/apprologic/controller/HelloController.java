package mg.apprologic.apprologic.controller;

import mg.apprologic.apprologic.model.article.Article;
import mg.apprologic.apprologic.services.article.ArticleService;
import mg.apprologic.apprologic.services.bons.BordereauFilleService;
import mg.apprologic.apprologic.services.stock.ReapproService;
import mg.apprologic.apprologic.services.stock.StockFilleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @Autowired
    ReapproService reapproService;

    @Autowired
    StockFilleService stockFilleService;

    @Autowired
    BordereauFilleService bordereauFilleService;

    @Autowired
    ArticleService articleService;
    @GetMapping("/")
    public String getIndex()
    {
        return "test";
    }
    @GetMapping("/getTest")
    public String getTest()
    {
        return "test";
    }

    @GetMapping("/loginPage")
    public String getLoginPage()
    {
        return "login";
    }

    @GetMapping("/getForms")
    public String getForms()
    {
        Article article = articleService.getById(4);
        Double prevision = bordereauFilleService.getPrevisionJournaliereArticle(article);
        double stockSecurite = bordereauFilleService.calculerStockSecurite(article);
        double rop = (prevision * Article.getDefaultDelayDemand()) + stockSecurite;
        System.out.println("Prevision journalière : "+prevision);
        System.out.println("Stock securté : "+stockSecurite);
        System.out.println("ROP : "+rop);
        return "forms";
    }

    @GetMapping("/getMailBox")
    public String getMailBox()
    {

        return "mailBox";
    }

    @GetMapping("/getBordereauTemplae")
    public String getBordereauTemplate()
    {
        return "bordereau";
    }
}
