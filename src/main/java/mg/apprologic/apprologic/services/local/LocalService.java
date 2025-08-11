package mg.apprologic.apprologic.services.local;

import mg.apprologic.apprologic.model.local.Local;
import mg.apprologic.apprologic.repository.local.LocalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocalService {

    @Autowired
    LocalRepository localRepository;

    public List<Local> getAllLocal()
    {
        return localRepository.findAll();
    }
    public Local getById(Integer id )
    {
        if (id == null) {
            return null;
        }
        return localRepository.getById(id);
    }
}
