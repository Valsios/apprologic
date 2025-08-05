package mg.apprologic.apprologic.repository.local;

import mg.apprologic.apprologic.model.local.GisementStockFille;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GisementStockFilleRepository extends JpaRepository<GisementStockFille,Integer> {
}
