package mg.apprologic.apprologic.services.bons;

import mg.apprologic.apprologic.model.bons.BonLivraisonMere;
import mg.apprologic.apprologic.repository.bons.BonLivraisonMereRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BonLivraisonMereService {

    @Autowired
    BonLivraisonMereRepository bonLivraisonMereRepository;

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
