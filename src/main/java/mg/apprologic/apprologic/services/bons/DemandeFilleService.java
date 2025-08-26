package mg.apprologic.apprologic.services.bons;

import mg.apprologic.apprologic.model.bons.DemandeFille;

import mg.apprologic.apprologic.model.bons.DemandeMere;
import mg.apprologic.apprologic.repository.bons.DemandeFilleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemandeFilleService {

    @Autowired
    DemandeFilleRepository demandeFilleRepository;

    public void save(DemandeFille demandeFille)
    {
        demandeFilleRepository.save(demandeFille);
    }

    public void delete(DemandeFille demandeFille)
    {
        demandeFilleRepository.delete(demandeFille);
    }

    public List<DemandeFille> getByMere(DemandeMere demandeMere)
    {
        return demandeFilleRepository.getDemandeFilleByDemandeMere(demandeMere);
    }
}
