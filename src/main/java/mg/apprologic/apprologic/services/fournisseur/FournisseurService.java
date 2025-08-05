package mg.apprologic.apprologic.services.fournisseur;

import mg.apprologic.apprologic.model.fournisseur.Fournisseur;
import mg.apprologic.apprologic.repository.fournisseur.FournisseurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FournisseurService {
    @Autowired
    FournisseurRepository fournisseurRepository;

    public List<Fournisseur> getAll()
    {
        return fournisseurRepository.findAll();
    }

    public void save(Fournisseur fournisseur)
    {
        fournisseurRepository.save(fournisseur);
    }
    public Fournisseur getById(Integer id)
    {
        return fournisseurRepository.getById(id);
    }
}
