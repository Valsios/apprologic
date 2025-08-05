package mg.apprologic.apprologic.services.stock;

import mg.apprologic.apprologic.model.stock.AnomalieStock;
import mg.apprologic.apprologic.model.stock.StockFille;
import mg.apprologic.apprologic.model.stock.StockMere;
import mg.apprologic.apprologic.repository.stock.StockMereRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class StockMereService {

    @Autowired
    StockMereRepository stockMereRepository;

    @Autowired
    StockFilleService stockFilleService;

    public void save(StockMere stockMere)
    {
        stockMereRepository.save(stockMere);
    }

    public void delete(StockMere stockMere)
    {
        stockMereRepository.delete(stockMere);
    }

    public List<StockMere> stockMeresAnomalie()
    {
        return stockMereRepository.getStockMereByDemandeMereIsNullAndBonLivraisonMereIsNull();
    }

    public List<AnomalieStock> getAnomaliesStock()
    {
        List<StockMere> stockMeresAnomalie = stockMeresAnomalie();

        List<AnomalieStock> anomalieStocks = new ArrayList<AnomalieStock>();
        for (StockMere stockMere : stockMeresAnomalie)
        {
            List<StockFille> stockFilleList = stockFilleService.getByMere(stockMere);
            StockFille stockFille = stockFilleList.get(0);

            AnomalieStock anomalieStock = new AnomalieStock();
            anomalieStock.setArticle(stockFille.getArticle());
            anomalieStock.setStock_theorique(stockFilleService.stock_theorique_article_date(stockMere.getDateMouvement(),stockFille.getArticle()));
            anomalieStock.setStock_physique(stockFilleService.stock_article_date(stockMere.getDateMouvement(),stockFille.getArticle()));
            anomalieStock.setDescription(stockMere.getDescription());
            anomalieStock.setDateInventaire(stockMere.getDateMouvement());

            anomalieStocks.add(anomalieStock);
        }

        return anomalieStocks;
    }
}
