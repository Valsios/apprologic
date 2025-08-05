package mg.apprologic.apprologic.services.stock;

import mg.apprologic.apprologic.model.article.Article;

import mg.apprologic.apprologic.model.stock.ReapproNotification;
import mg.apprologic.apprologic.services.article.ArticleService;
import mg.apprologic.apprologic.services.bons.BordereauFilleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReapproService {

    @Autowired
    ArticleService articleService;

    @Autowired
    BordereauFilleService bordereauFilleService;



    @Autowired
    StockFilleService stockFilleService;

    public List<ReapproNotification> verifierStock() {
        List<ReapproNotification> reapproNotificationList = new ArrayList<ReapproNotification>();
        articleService.getAll().forEach(article -> {
            Double enStockArticle = stockFilleService.stock_article_date(LocalDateTime.now(),article);
            double consommationJournaliere = bordereauFilleService.getPrevisionJournaliereArticle(article);
            double rop = (consommationJournaliere * Article.getDefaultDelayDemand()) + bordereauFilleService.calculerStockSecurite(article);

            System.out.println("article "+article.getDesignation() +" en stock :"+enStockArticle+" rop : "+rop);
            if (enStockArticle< rop) {
               ReapproNotification reapproNotification = new ReapproNotification();
               reapproNotification.setArticle(article);

               reapproNotification.setQuantiteRecommandee(rop-enStockArticle);
               reapproNotification.setDateCreation(LocalDateTime.now());

               reapproNotificationList.add(reapproNotification);

            }
        });
        return reapproNotificationList;
    }
}
