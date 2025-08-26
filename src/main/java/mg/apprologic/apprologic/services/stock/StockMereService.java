package mg.apprologic.apprologic.services.stock;

import mg.apprologic.apprologic.model.stock.AnomalieStock;
import mg.apprologic.apprologic.model.stock.StockFille;
import mg.apprologic.apprologic.model.stock.StockMere;
import mg.apprologic.apprologic.repository.stock.StockMereRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    public StockMere getById(Integer id)
    {
        return stockMereRepository.getById(id);
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
            if (anomalieStock.getStock_theorique() - anomalieStock.getStock_physique()== 0.0)
            {
                String newDesc = "Stock retabli.";

                if (stockFille.getEntree()>stockFille.getSortie())
                {
                    newDesc +="( par excedent de "+stockFille.getEntree()+" )";
                }
                else
                {
                    newDesc +="( par deficit de "+stockFille.getSortie()+" )";
                }
                anomalieStock.setDescription(newDesc);
            }
            anomalieStock.setDateInventaire(stockMere.getDateMouvement());
            anomalieStocks.add(anomalieStock);
        }

        return anomalieStocks;
    }
}
