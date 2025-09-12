package mg.apprologic.apprologic.services.bons;

import mg.apprologic.apprologic.model.article.Article;
import mg.apprologic.apprologic.model.bons.BordereauFille;
import mg.apprologic.apprologic.model.bons.BordereauMere;
import mg.apprologic.apprologic.model.consommateur.Consommateur;
import mg.apprologic.apprologic.repository.bons.BordereauFilleRepository;
import mg.apprologic.apprologic.repository.consommateur.ConsommateurRepository;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@Service
public class BordereauFilleService {

    @Autowired
    BordereauFilleRepository bordereauFilleRepository;


    @Autowired
    ConsommateurRepository consommateurRepository;

    public void save(BordereauFille bordereauFille)
    {
        bordereauFilleRepository.save(bordereauFille);
    }

    public void delete(BordereauFille bordereauFille)
    {
        bordereauFilleRepository.delete(bordereauFille);
    }



    //consommation des departements
    public HashMap<Article,Double> consommationDepartementParArticle(Integer year,Consommateur consommateur,Integer month)
    {
        if (month != null)
        {
            month = month+1;
        }
        List<BordereauFille> bordereauFilleList = bordereauFilleRepository.getBordereauFilleByConsommateurAndYearAndMonth(consommateur,year,month);
        HashMap<Article,Double> toReturn = new HashMap<>();
        for (BordereauFille bordereauFille : bordereauFilleList)
        {
            Article article = bordereauFille.getDemandeFille().getArticle();

            if (toReturn.containsKey(article))
            {
                toReturn.put(article,toReturn.get(article)+bordereauFille.getQuantiteSortie());
            }
            else
            {
                toReturn.put(article,bordereauFille.getQuantiteSortie());
            }
        }

        return toReturn;
    }
    //filtre article and year

    public HashMap<String,Double> tauxSatisfactionDemande(List<BordereauFille> bordereauFilleList)
    {

        HashMap<String, Double> toReturn = new HashMap<>() ;
        Double lack = 0.0;

        Double quantiteDemande = 0.0;
        Double quantiteSortie = 0.0;
        for (BordereauFille bordereauFille : bordereauFilleList)
        {
            quantiteDemande += bordereauFille.getDemandeFille().getQuantite();
            quantiteSortie += bordereauFille.getQuantiteSortie();
            lack += (bordereauFille.getQuantiteSortie()/bordereauFille.getDemandeFille().getQuantite())*100;
        }
        lack = lack / bordereauFilleList.size();

        toReturn.put("tauxSatisfactionDemande",lack);
        toReturn.put("quantiteDemande",quantiteDemande);
        toReturn.put("quantiteSortie",quantiteSortie);
        return toReturn;

    }

    public HashMap<Consommateur,Double> departementPlusConsommateur(List<BordereauFille> bordereauFilleList)
    {


        HashMap<Consommateur,Double> toReturn = new HashMap<>();
        List<Consommateur> consommateurList = consommateurRepository.findAll();
        for (Consommateur consommateur : consommateurList)
        {
            toReturn.put(consommateur,0.0);
        }
        for (BordereauFille bordereauFille : bordereauFilleList)
        {
            Consommateur consommateur = (bordereauFille.getBordereauMere().getDemandeMere().getConsommateur());
            toReturn.put(consommateur,toReturn.get(consommateur)+bordereauFille.getQuantiteSortie());

        }
        System.out.println(toReturn);
        return toReturn;
    }

    //END OF DASHBOARD
    public List<BordereauFille> getBordereauFilleByArticleAndYear(Article article,Integer year)
    {
        return bordereauFilleRepository.getBordereauFilleByArticleAndYear(article,year);
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

        if (historique.isEmpty()) {
            return 0.0;
        }

        double alpha = 0.3; // Facteur de pondération (0 < alpha < 1)
        double prevision = historique.get(0).getQuantiteSortie();
        System.out.println("Prévision : "+prevision+" avec demande réel :"+prevision+" et Date : "+historique.get(0).getBordereauMere().getDateBordereau());
        for (int i = 1; i < historique.size(); i++) {
            double demandeReelle = historique.get(i).getQuantiteSortie();

            prevision = alpha * demandeReelle + (1 - alpha) * prevision;
            System.out.println("Prévision : "+prevision);
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
        // Si trop peu de données
        if (demandes.length < 2) {
            return 0.0;
        }
        Arrays.sort(demandes);
        double mediane = calculateMedian(demandes);
        double mad = calculateMAD(demandes, mediane);
        double lowerBound = mediane - 3*mad;
        double upperBound = mediane + 3*mad;
        double[] filteredDemandes = Arrays.stream(demandes)
                .filter(d -> d >= lowerBound && d <= upperBound)
                .toArray();
        //fall back si trop peu de donnée
        if(filteredDemandes.length<2)
        {
            filteredDemandes = new double[]{mediane};
        }
        // Recalcul avec données filtrées
        double zScore = 1.65; //1.65 pour 95% de service
        DescriptiveStatistics stats = new DescriptiveStatistics(filteredDemandes);
        return zScore * stats.getStandardDeviation() * Math.sqrt(Article.getDefaultDelayDemand());
    }

    // Médiane
    private double calculateMedian(double[] data) {
        Arrays.sort(data);
        int n = data.length;
        if (n % 2 == 0) {
            return (data[n/2 - 1] + data[n/2]) / 2.0;
        } else {
            return data[n/2];
        }
    }

    // Median Absolute Deviation
    private double calculateMAD(double[] data, double median) {
        double[] absoluteDeviations = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            absoluteDeviations[i] = Math.abs(data[i] - median);
        }
        Arrays.sort(absoluteDeviations);
        return calculateMedian(absoluteDeviations);
    }


}
