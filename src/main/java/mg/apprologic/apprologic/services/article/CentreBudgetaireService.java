package mg.apprologic.apprologic.services.article;


import mg.apprologic.apprologic.model.article.CentreBudgetaire;
import mg.apprologic.apprologic.repository.article.CentreBudgetaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CentreBudgetaireService {


    @Autowired
    CentreBudgetaireRepository centreBudgetaireRepository;

    public List<CentreBudgetaire> getAll()
    {
        return centreBudgetaireRepository.findAll();
    }

    public CentreBudgetaire getById(Integer id)
    {
        return centreBudgetaireRepository.getById(id);
    }
}
