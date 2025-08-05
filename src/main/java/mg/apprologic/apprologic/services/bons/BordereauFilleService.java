package mg.apprologic.apprologic.services.bons;

import mg.apprologic.apprologic.model.article.Article;
import mg.apprologic.apprologic.model.bons.BordereauFille;
import mg.apprologic.apprologic.model.bons.BordereauMere;
import mg.apprologic.apprologic.repository.bons.BordereauFilleRepository;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
public class BordereauFilleService {

    @Autowired
    BordereauFilleRepository bordereauFilleRepository;

    public void save(BordereauFille bordereauFille)
    {
        bordereauFilleRepository.save(bordereauFille);
    }

    public void delete(BordereauFille bordereauFille)
    {
        bordereauFilleRepository.delete(bordereauFille);
    }

    public List<BordereauFille> getAllByMere(BordereauMere bordereauMere)
    {
        return bordereauFilleRepository.getBordereauFilleByBordereauMere(bordereauMere);
    }

    public Double getPrevisionJournaliereArticle(Article article) {

        LocalDateTime minus6Months = LocalDateTime.now().minusMonths(6);
        LocalDateTime present = LocalDateTime.now();
        List<BordereauFille> historique = bordereauFilleRepository
                .getByArticleAndDateBetween(article, minus6Months, present)
                .stream()
                .sorted(Comparator.comparing(bf -> bf.getBordereauMere().getDateBordereau()))
                .toList();

        double alpha = 0.3; // Facteur de pondération (0 < alpha < 1)
        double prevision = historique.isEmpty() ? 0 : historique.get(0).getQuantiteSortie();

        for (BordereauFille bf : historique) {
            prevision = alpha * bf.getQuantiteSortie() + (1 - alpha) * prevision;
        }
        return prevision;
    }

    public double calculerStockSecurite(Article article) {
        LocalDateTime present = LocalDateTime.now();
        LocalDateTime minus6Months = present.minusMonths(6);

        List<BordereauFille> historique = bordereauFilleRepository.getByArticleAndDateBetween(article,minus6Months,present);
        double[] demandes = historique.stream()
                .mapToDouble(BordereauFille::getQuantiteSortie)
                .toArray();

        // Exclusion des valeurs hors intervalle [moyenne ± 2σ]
        DescriptiveStatistics stats = new DescriptiveStatistics(demandes);
        double lowerBound = stats.getMean() - 2 * stats.getStandardDeviation();
        double upperBound = stats.getMean() + 2 * stats.getStandardDeviation();

        double[] filteredDemandes = Arrays.stream(demandes)
                .filter(d -> d >= lowerBound && d <= upperBound)
                .toArray();

        // Recalcul avec données filtrées
        double zScore = 1.65; //1.65 pour 95% de service
        stats = new DescriptiveStatistics(filteredDemandes);
        return zScore * stats.getStandardDeviation() * Math.sqrt(Article.getDefaultDelayDemand() / 30.0);
    }


}
