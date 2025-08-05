package mg.apprologic.apprologic.services.consommateur;

import mg.apprologic.apprologic.model.consommateur.Consommateur;
import mg.apprologic.apprologic.repository.consommateur.ConsommateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsommateurService {

    @Autowired
    ConsommateurRepository consommateurRepository;

    public List<Consommateur> getAllMere()
    {
        return consommateurRepository.findConsommateurByMereIsNull();
    }

    public List<Consommateur> getAllEnfant(Consommateur consommateur)
    {
        return consommateurRepository.findConsommateurByMere(consommateur);
    }

    public Consommateur getById(Integer id)
    {
        return consommateurRepository.getById(id);
    }
}
