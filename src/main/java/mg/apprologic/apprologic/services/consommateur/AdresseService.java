package mg.apprologic.apprologic.services.consommateur;

import mg.apprologic.apprologic.model.consommateur.Adresse;
import mg.apprologic.apprologic.repository.consommateur.AdresseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdresseService {

    @Autowired
    AdresseRepository adresseRepository;

    public List<Adresse> getAll()
    {
        return adresseRepository.findAll();
    }

    public Adresse getById(Integer id)
    {
        return adresseRepository.getById(id);
    }
}
