package mg.apprologic.apprologic.repository.bons;

import mg.apprologic.apprologic.model.bons.DemandeFille;
import mg.apprologic.apprologic.model.bons.DemandeMere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandeFilleRepository extends JpaRepository<DemandeFille,Integer> {

    public List<DemandeFille> getDemandeFilleByDemandeMere(DemandeMere demandeMere);
}
