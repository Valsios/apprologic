package mg.apprologic.apprologic.services.local;

import mg.apprologic.apprologic.model.local.ExistantGisement;
import mg.apprologic.apprologic.repository.local.ExistantGisementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExistantGisementService {

    @Autowired
    ExistantGisementRepository existantGisementRepository;

    public ExistantGisement getById(Integer id)
    {
        return existantGisementRepository.getById(id);
    }
}


