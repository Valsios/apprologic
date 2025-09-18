package mg.apprologic.apprologic.services.bons;

import mg.apprologic.apprologic.model.bons.BonLivraisonFille;
import mg.apprologic.apprologic.model.bons.BonLivraisonMere;
import mg.apprologic.apprologic.model.bons.BordereauMere;
import mg.apprologic.apprologic.model.fournisseur.Fournisseur;
import mg.apprologic.apprologic.model.stock.StockMere;
import mg.apprologic.apprologic.repository.bons.BonLivraisonMereRepository;
import mg.apprologic.apprologic.services.fournisseur.FournisseurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
public class BonLivraisonMereService {

    @Autowired
    BonLivraisonMereRepository bonLivraisonMereRepository;

    @Autowired
    BonLivraisonFilleService bonLivraisonFilleService;

    @Autowired
    FournisseurService fournisseurService;


    @Transactional(readOnly = true)

    public List<BonLivraisonMere> getByFournisseurDate(String idFourniseur, String debut , String fin)
    {
        return bonLivraisonMereRepository.getByFournisseurDateNative(idFourniseur,debut,fin);
    }

    public boolean checkIfExist(List<BonLivraisonMere> bonLivraisonMereList,Fournisseur fournisseur)
    {
        for(BonLivraisonMere bonLivraisonMere : bonLivraisonMereList)
        {
            if (fournisseur == bonLivraisonMere.getFournisseur())
            {
                return true;
            }
        }
        return false;
    }



    //manomboka eto
    public HashMap<Fournisseur,Double> satisfactionFournisseurGlobal(List<BonLivraisonMere> bonLivraisonMereList)
    {

        HashMap<Fournisseur,Double> toReturn = new HashMap<>();
        List<Fournisseur> fournisseurList= fournisseurService.getAll();
        for (Fournisseur fournisseur : fournisseurList)
        {
            if (checkIfExist(bonLivraisonMereList,fournisseur))
            {
                toReturn.put(fournisseur,100.0);
            }
            else{
                toReturn.put(fournisseur,0.0);
            }

        }

        for (BonLivraisonMere bonLivraisonMere : bonLivraisonMereList)
        {
            Double satisfactionLivraison = satisfactionLivraison(bonLivraisonMere);
            Fournisseur fournisseur = bonLivraisonMere.getFournisseur();
            Double newValue = (toReturn.get(fournisseur)+satisfactionLivraison)/2;
            toReturn.put(fournisseur,newValue);

        }
        return toReturn;
    }

    public HashMap<String,Double> satisfactionParLivraison(List<BonLivraisonMere> bonLivraisonMereList)
    {

        HashMap<String,Double> toReturn = new HashMap<>();
        Integer quantiteSatisfait = 0;
        for (BonLivraisonMere bonLivraisonMere : bonLivraisonMereList)
        {
            if (Math.abs(satisfactionLivraison(bonLivraisonMere)-100)<0.01)
            {
                quantiteSatisfait += 1;
            }
        }
        System.out.println("QTE satisfait : "+quantiteSatisfait);
        System.out.println("Count livraison : "+bonLivraisonMereList.size());
        Double satisfait =(quantiteSatisfait.doubleValue()/(double) (bonLivraisonMereList.size()))*100;
        Double nonSatisfait = 100-satisfait;

        toReturn.put("satisfait",satisfait);
        toReturn.put("nonSatisfait",nonSatisfait);

        return toReturn;

    }

    public Double satisfactionLivraison(BonLivraisonMere bonLivraisonMere)
    {
        Double taux = 0.0;

        List<BonLivraisonFille> bonLivraisonFilleList  = bonLivraisonFilleService.getByMere(bonLivraisonMere);
        for (BonLivraisonFille bonLivraisonFille : bonLivraisonFilleList)
        {
            taux += (bonLivraisonFille.getQuantite_recu()/bonLivraisonFille.getQuantite_demande())*100;
        }
        taux = taux / bonLivraisonFilleList.size();
        return taux;
    }
    public Integer countLivraison(List<BonLivraisonMere> bonLivraisonMereList)
    {
        return bonLivraisonMereList.size();
    }

    @Transactional(readOnly = true)
    public List<BonLivraisonMere> getByYear(Integer year)
    {
        return bonLivraisonMereRepository.getBonLivraisonMereByYear(year);
    }

    //tapitra eto
    @Transactional
    public byte[] getPieceJointeById(Integer livraisonId) {
        BonLivraisonMere bon = bonLivraisonMereRepository.findById(livraisonId).orElseThrow();
        return bon.getPieceJointe();
    }


    public void save(BonLivraisonMere bonLivraisonMere)
    {
        bonLivraisonMereRepository.save(bonLivraisonMere);
    }
    public void delete(BonLivraisonMere bonLivraisonMere)
    {
        bonLivraisonMereRepository.delete(bonLivraisonMere);
    }

    public List<BonLivraisonMere> getAll()
    {
        return bonLivraisonMereRepository.findAll();
    }

    public BonLivraisonMere getById(Integer id)
    {
        return bonLivraisonMereRepository.getById(id);
    }
}
