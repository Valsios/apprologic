package mg.apprologic.apprologic.services.bons;

import mg.apprologic.apprologic.model.bons.BordereauMere;
import mg.apprologic.apprologic.model.consommateur.Consommateur;
import mg.apprologic.apprologic.repository.bons.BordereauMereRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BordereauMereService {

    @Autowired
    BordereauMereRepository bordereauMereRepository;


    public List<BordereauMere> getByConsommateur(Consommateur consommateur)
    {
        return bordereauMereRepository.getBordereauMereByConsommateur(consommateur);
    }
    public BordereauMere getById(Integer id)
    {
        return bordereauMereRepository.getById(id);
    }
    public void save(BordereauMere bordereauMere)
    {
        bordereauMereRepository.save(bordereauMere);
    }

    public void delete(BordereauMere bordereauMere)
    {
        bordereauMereRepository.delete(bordereauMere);
    }

    public List<BordereauMere> getAll()
    {
        return bordereauMereRepository.findAll();
    }
}
