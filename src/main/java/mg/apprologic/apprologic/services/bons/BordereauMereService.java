package mg.apprologic.apprologic.services.bons;

import mg.apprologic.apprologic.model.bons.BordereauFille;
import mg.apprologic.apprologic.model.bons.BordereauMere;
import mg.apprologic.apprologic.model.consommateur.Consommateur;
import mg.apprologic.apprologic.repository.bons.BordereauFilleRepository;
import mg.apprologic.apprologic.repository.bons.BordereauMereRepository;
import mg.apprologic.apprologic.repository.bons.DemandeMereRepository;
import mg.apprologic.apprologic.repository.consommateur.ConsommateurRepository;
import mg.apprologic.apprologic.services.consommateur.ConsommateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BordereauMereService {

    @Autowired
    BordereauMereRepository bordereauMereRepository;

    @Autowired
    BordereauFilleService bordereauFilleService;

    @Autowired
    DemandeMereService demandeMereService;

    @Autowired
    ConsommateurRepository consommateurRepository;


    //evolution expense

    public List<BordereauMere> getByConsommateurDate(String consommateurId,String debut,String fin)
    {
        return bordereauMereRepository.getByConsommateurDateNative(consommateurId,debut,fin);
    }

    public HashMap<Integer, Double> evolutionDepense(List<BordereauMere> bordereauMereList) {
        HashMap<Integer, Double> toReturn = new HashMap<>();

        if (bordereauMereList == null || bordereauMereList.isEmpty()) {
            return toReturn;
        }

        // Obtenir le mois courant
        LocalDateTime now = LocalDateTime.now();
        int currentMonth = now.getMonthValue();
        int currentYear = now.getYear();

        // Déterminer l'année des bordereaux (à partir du premier élément)
        int dataYear = bordereauMereList.get(0).getDateBordereau().getYear();

        // Déterminer combien de mois traiter
        int monthsToProcess = 12;
        if (dataYear == currentYear) {
            monthsToProcess = currentMonth;
        }

        // Initialiser tous les mois avec 0.0
        for (int month = 1; month <= monthsToProcess; month++) {
            toReturn.put(month, 0.0);
        }

        // Parcourir tous les bordereaux et accumuler les dépenses par mois
        for (BordereauMere bordereau : bordereauMereList) {
            int month = bordereau.getDateBordereau().getMonthValue();

            // Vérifier que le mois est dans la plage valide
            if (month >= 1 && month <= monthsToProcess) {
                double valeur = valeurBordereau(bordereau); // Appel à votre fonction valeur()
                double currentValue = toReturn.get(month);
                toReturn.put(month, currentValue + valeur);
            }
        }

        return toReturn;
    }

    //consommation valeur en Ariary des departements

    public Double sommeDepese (List<BordereauMere> bordereauMereList)
    {
        Double somme = 0.0;
        for (BordereauMere bordereauMere : bordereauMereList)
        {
            somme += valeurBordereau(bordereauMere);
        }
        return somme;
    }

    //ito averina

    public double consommationValeurAll(HashMap<?,Double> consommation)
    {
        double toReturn = 0.0;
        for (Map.Entry<?,Double> map : consommation.entrySet())
        {
            toReturn += map.getValue();
        }
        return toReturn;
    }
    public HashMap<Consommateur,Double> consommationValeurParDepartement(List<BordereauMere> bordereauMereList)
    {

        HashMap<Consommateur,Double> toReturn = new HashMap<>();
        List<Consommateur> consommateurList = consommateurRepository.findAll();
        for (Consommateur consommateur : consommateurList)
        {
            toReturn.put(consommateur,0.0);
        }
        for (BordereauMere bordereauMere : bordereauMereList)
        {
            Double valeurBordereau = valeurBordereau(bordereauMere);
            Consommateur consommateur = bordereauMere.getDemandeMere().getConsommateur();
            Double newValue = toReturn.get(consommateur) + valeurBordereau;
            toReturn.put(consommateur,newValue);
        }
        return toReturn;
    }

    public Double valeurBordereau(BordereauMere bordereauMere)
    {
        List<BordereauFille> bordereauFilleList = bordereauFilleService.getAllByMere(bordereauMere);
        Double value = 0.0;
        for (BordereauFille bordereauFille : bordereauFilleList)
        {
            value += bordereauFille.getQuantiteSortie()*bordereauFille.getPrixUnitaire()*bordereauMere.getDevise().getCoursAriary();
        }
        return value;
    }

    //map des chiffres concernant les demandes

    //ito averina
    public HashMap<String,Double> totalDemande(Integer year,List<BordereauMere> bordereauMereList)
    {
        HashMap<String,Double> toReturn = new HashMap<>();
        Integer countDemande = demandeMereService.countDemandeMere(year);
        Integer countDemandeSortie = bordereauMereList.size();
        Integer countNonSortie = countDemande - countDemandeSortie;

        double taux = 0.0;

        for (BordereauMere bordereauMere : bordereauMereList)
        {
            taux += satisfactionBordereau(bordereauMere);
        }
        taux = taux/bordereauMereList.size();
        toReturn.put("demande",countDemande.doubleValue());
        toReturn.put("sortie",countDemandeSortie.doubleValue());
        toReturn.put("nonSortie",countNonSortie.doubleValue());
        toReturn.put("tauxSatisfaction",taux);
        toReturn.put("somme",sommeDepese(bordereauMereList));

        return toReturn;

    }
    public Double satisfactionBordereau(BordereauMere bordereauMere)
    {
        List<BordereauFille> bordereauFilleList = bordereauFilleService.getAllByMere(bordereauMere);

        double taux = 0.0;
        for (BordereauFille bordereauFille : bordereauFilleList)
        {
            taux += (bordereauFille.getQuantiteSortie()/bordereauFille.getDemandeFille().getQuantite())*100;
        }
        return taux/bordereauFilleList.size();
    }
    public List<BordereauMere> getByYear(Integer year)
    {
        return bordereauMereRepository.getBordereauMereByYear(year);
    }
    public BordereauMere getBordereauMereByDemande(Integer idDemandeMere)
    {
        return bordereauMereRepository.getBordereauMereByIdDemandeMere(idDemandeMere);
    }
    public List<BordereauMere> getByConsommateur(Consommateur consommateur)
    {
        return bordereauMereRepository.getBordereauMereByConsommateur(consommateur);
    }


    public BordereauMere getById(Integer id)
    {
        return bordereauMereRepository.getById(id);
    }
    public void save(BordereauMere bordereauMere)
    {
        bordereauMereRepository.save(bordereauMere);
    }

    public void delete(BordereauMere bordereauMere)
    {
        bordereauMereRepository.delete(bordereauMere);
    }

    public List<BordereauMere> getAll()
    {
        return bordereauMereRepository.findAll();
    }
}
