package mg.apprologic.apprologic.services.stock;

import mg.apprologic.apprologic.model.article.Article;
import mg.apprologic.apprologic.model.stock.StockFille;
import mg.apprologic.apprologic.model.stock.StockMere;
import mg.apprologic.apprologic.repository.stock.StockFilleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
public class StockFilleService {

    @Autowired
    StockFilleRepository stockFilleRepository;

    public void save (StockFille stockFille)
    {
        stockFilleRepository.save(stockFille);
    }
    public void delete (StockFille stockFille)
    {
        stockFilleRepository.delete(stockFille);
    }

    //filtre par article et year
    public HashMap<Integer, Double> evolutionMensuel(Integer year, Article article) {
        HashMap<Integer, Double> toReturn = new LinkedHashMap<>();
        LocalDateTime now = LocalDateTime.now();
        int currentYear = now.getYear();
        int currentMonth = now.getMonthValue();
        int monthsToProcess = 12;
        if (year.equals(currentYear)) {
            monthsToProcess = currentMonth ;
        }

        // Pour chaque mois, récupérer le stock à la fin du mois
        for (int month = 1; month <= monthsToProcess; month++) {
            LocalDateTime endOfMonth = LocalDateTime.of(year, month, 1, 0, 0)
                    .with(TemporalAdjusters.lastDayOfMonth())
                    .withHour(23).withMinute(59).withSecond(59);

            Double stockValue = stock_article_date(endOfMonth,article);
            toReturn.put(month, stockValue != null ? stockValue : 0.0);
        }

        return toReturn;
    }
    public List<StockFille> getStockFilleByArticleAndYear(Article article,Integer year)
    {
        return stockFilleRepository.getStockFilleByArticleAndYear(article,year);
    }
    public List<StockFille> getStockFilleByArticleAndYearWithoutAnomalie(Article article,Integer year)
    {
        return stockFilleRepository.getStockFilleByArticleAndYearWithoutAnomalie(article,year);
    }

    public List<StockFille> stock_date(LocalDateTime dateTime,String designation)
    {

        List<StockFille> toReturn = new ArrayList<>();
        List<Object[]> liste = stockFilleRepository.findStockDateByDesignationIgnoreCase(dateTime,designation);
        for (Object[] temp : liste)
        {
            StockFille stockFille = new StockFille();
            System.out.println("Entry : "+temp[1]);
            stockFille.setArticle((Article) temp[0]);
            stockFille.setTotal_entree((Double) temp[1]);
            stockFille.setTotal_sortie((Double) temp[2]);
            stockFille.setLast_date((LocalDateTime) temp[3]);
            stockFille.setStock_date(stockFille.getTotal_entree()-stockFille.getTotal_sortie());
            toReturn.add(stockFille);

        }
        return toReturn;
    }

    public Double stock_article_date(LocalDateTime dateTime,Article article)
    {

        List<Object[]> dataTemp = stockFilleRepository.findStockFilleByArticle(dateTime,article);
        if (dataTemp.size()==0)
        {
            return 0.0;
        }
        Object[] data = dataTemp.get(0);
        StockFille stockFilleTemp = new StockFille();
        stockFilleTemp.setArticle((Article) data[0]);
        stockFilleTemp.setTotal_entree((Double) data[1]);
        stockFilleTemp.setTotal_sortie((Double) data[2]);
        stockFilleTemp.setLast_date((LocalDateTime) data[3]);
        stockFilleTemp.setStock_date(stockFilleTemp.getTotal_entree()-stockFilleTemp.getTotal_sortie());
        return stockFilleTemp.getStock_date();
    }

    public Double stock_theorique_article_date(LocalDateTime dateTime,Article article)
    {

        List<Object[]> dataTemp = stockFilleRepository.findStockFilleByArticleTheorique(dateTime,article);
        Object[] data = dataTemp.get(0);
        StockFille stockFilleTemp = new StockFille();
        stockFilleTemp.setArticle((Article) data[0]);
        stockFilleTemp.setTotal_entree((Double) data[1]);
        stockFilleTemp.setTotal_sortie((Double) data[2]);
        stockFilleTemp.setLast_date((LocalDateTime) data[3]);
        stockFilleTemp.setStock_date(stockFilleTemp.getTotal_entree()-stockFilleTemp.getTotal_sortie());
        return stockFilleTemp.getStock_date();
    }

    public List<StockFille> getByMere(StockMere stockMere)
    {
        return stockFilleRepository.getStockFilleByStockMere(stockMere);
    }

}
