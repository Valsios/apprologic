package mg.apprologic.apprologic.controller;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import mg.apprologic.apprologic.model.article.Article;
import mg.apprologic.apprologic.services.article.ArticleService;
import mg.apprologic.apprologic.services.bons.BordereauFilleService;
import mg.apprologic.apprologic.services.importUtil.ImportService;
import mg.apprologic.apprologic.services.stock.ReapproService;
import mg.apprologic.apprologic.services.stock.StockFilleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.FileReader;
import java.util.List;

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


    @Autowired
    ImportService importService;

    @GetMapping("/data/importAll")
    public String importAll()throws Exception
    {
        CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
        String filePath = "/Users/randriamalalavalisoa/STAGE/DATA.csv";
        CSVReader reader = new CSVReaderBuilder(new FileReader(filePath))
                .withCSVParser(parser)
                .build();

        List<String[]> allData = reader.readAll(); // Lire toutes les lignes
        reader.close();

        // Ignorer la première ligne (en-têtes)
        allData.remove(0);

        for (String[] data : allData)
        {
            importService.saveLine(data);
        }
        return "redirect:/";
    }
    @GetMapping("/")
    public String getIndex()
    {
        return "redirect:/analyse/dashboard";
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
