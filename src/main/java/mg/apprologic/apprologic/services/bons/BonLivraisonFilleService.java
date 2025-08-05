package mg.apprologic.apprologic.services.bons;

import mg.apprologic.apprologic.model.bons.BonLivraisonFille;
import mg.apprologic.apprologic.repository.bons.BonLivraisonFilleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BonLivraisonFilleService {

    @Autowired
    BonLivraisonFilleRepository bonLivraisonFilleRepository;

    public void save(BonLivraisonFille bonLivraisonFille)
    {
        bonLivraisonFilleRepository.save(bonLivraisonFille);
    }

    public void delete(BonLivraisonFille bonLivraisonFille)
    {
        bonLivraisonFilleRepository.delete(bonLivraisonFille);
    }

}
