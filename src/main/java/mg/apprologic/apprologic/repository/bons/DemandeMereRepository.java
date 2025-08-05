package mg.apprologic.apprologic.repository.bons;

import mg.apprologic.apprologic.model.bons.DemandeMere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandeMereRepository extends JpaRepository<DemandeMere,Integer> {

    public List<DemandeMere> getDemandeMereByDateSortieIsNull();
}
