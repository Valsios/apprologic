package mg.apprologic.apprologic.repository.bons;

import mg.apprologic.apprologic.model.bons.DemandeMere;
import mg.apprologic.apprologic.model.consommateur.Consommateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandeMereRepository extends JpaRepository<DemandeMere,Integer> {

    public List<DemandeMere> getDemandeMereByDateSortieIsNull();

    @Query("SELECT count(d) FROM DemandeMere d where year (d.dateDemande) = :year")
    public Integer countDemandeMereByYear(Integer year);

    public List<DemandeMere> getDemandeMereByConsommateurAndDateSortieIsNull(Consommateur consommateur);
}
