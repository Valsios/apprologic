package mg.apprologic.apprologic.services.consommateur;

import mg.apprologic.apprologic.model.consommateur.Transport;
import mg.apprologic.apprologic.repository.consommateur.TransportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransportService {

    @Autowired
    TransportRepository transportRepository;

    public List<Transport> getAll()
    {
        return transportRepository.findAll();
    }

    public Transport getById(Integer id)
    {
        return transportRepository.getById(id);
    }
}
