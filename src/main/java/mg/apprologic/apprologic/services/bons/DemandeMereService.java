package mg.apprologic.apprologic.services.bons;

import mg.apprologic.apprologic.model.bons.DemandeMere;
import mg.apprologic.apprologic.model.consommateur.Consommateur;
import mg.apprologic.apprologic.repository.bons.DemandeMereRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemandeMereService {

    @Autowired
    DemandeMereRepository demandeMereRepository;


    public List<DemandeMere> getByConsommateur(Consommateur consommateur ) { return demandeMereRepository.getDemandeMereByConsommateurAndDateSortieIsNull(consommateur);}

    public DemandeMere getById(Integer id)
    {
        return demandeMereRepository.getById(id);
    }
    public List<DemandeMere> getAllNotSortie()
    {
        return demandeMereRepository.getDemandeMereByDateSortieIsNull();
    }
    public DemandeMere save(DemandeMere demandeMere)
    {
        return demandeMereRepository.save(demandeMere);
    }

    public void delete(DemandeMere demandeMere)
    {
        demandeMereRepository.delete(demandeMere);
    }

}
