package mg.apprologic.apprologic.services.article;

import mg.apprologic.apprologic.model.article.Devise;
import mg.apprologic.apprologic.repository.article.DeviseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviseService {

    @Autowired
    DeviseRepository deviseRepository;

    public List<Devise> getAll()
    {
        return deviseRepository.findAll();
    }

    public Devise getById(Integer id)
    {
        return deviseRepository.getById(id);
    }

    public void save(Devise devise)
    {
        deviseRepository.save(devise);
    }
}
