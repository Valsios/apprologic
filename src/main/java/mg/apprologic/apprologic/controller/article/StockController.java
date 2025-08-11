package mg.apprologic.apprologic.controller.article;

import mg.apprologic.apprologic.model.stock.StockFille;
import mg.apprologic.apprologic.services.stock.StockFilleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/article")
public class StockController {

    @Autowired
    StockFilleService stockFilleService;

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
