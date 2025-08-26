package mg.apprologic.apprologic.services.local;

import mg.apprologic.apprologic.model.local.ExistantGisement;
import mg.apprologic.apprologic.model.local.Local;
import mg.apprologic.apprologic.repository.local.ExistantGisementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExistantGisementService {

    @Autowired
    ExistantGisementRepository existantGisementRepository;


    public List<ExistantGisement> getNeverUsed(Local local)
    {
        return existantGisementRepository.getNeverUsed(local);
    }
    public ExistantGisement getById(Integer id)
    {
        return existantGisementRepository.getById(id);
    }

    public void save(ExistantGisement existantGisement)
    {
        existantGisementRepository.save(existantGisement);
    }
}


