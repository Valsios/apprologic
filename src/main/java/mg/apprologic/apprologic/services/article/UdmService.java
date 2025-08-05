package mg.apprologic.apprologic.services.article;

import mg.apprologic.apprologic.model.article.Udm;
import mg.apprologic.apprologic.repository.article.UdmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UdmService {
    @Autowired
    UdmRepository udmRepository;

    public List<Udm> getAll()
    {
        return udmRepository.findAll();
    }

    public Udm getById(Integer id)
    {
        return udmRepository.getById(id);
    }
}

