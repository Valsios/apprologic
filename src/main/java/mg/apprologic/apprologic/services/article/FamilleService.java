package mg.apprologic.apprologic.services.article;

import mg.apprologic.apprologic.model.article.Famille;
import mg.apprologic.apprologic.repository.article.FamilleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FamilleService {

    @Autowired
    FamilleRepository familleRepository;

    public List<Famille> getAll()
    {
        return familleRepository.findAll();
    }

    public Famille getById(Integer id)
    {
        return familleRepository.getById(id);
    }
}
