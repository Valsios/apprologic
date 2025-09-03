package mg.apprologic.apprologic.repository.local;

import mg.apprologic.apprologic.model.local.ExistantGisement;
import mg.apprologic.apprologic.model.local.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExistantGisementRepository extends JpaRepository<ExistantGisement,Integer> {

    @Query("SELECT e FROM ExistantGisement e " +
            "WHERE NOT EXISTS (SELECT g FROM GisementArticle g WHERE g.gisement = e) AND (:local IS NULL OR e.local = :local)")
    public List<ExistantGisement> getNeverUsed(Local local);

    @Query("SELECT e FROM ExistantGisement e where e.local = :local AND e.trave = :trave AND e.alveole = :alveole AND e.etagere = :etagere AND e.bac = :bac ")
    public ExistantGisement getByAll(Local local,String trave,String alveole,String etagere,String bac);
}
