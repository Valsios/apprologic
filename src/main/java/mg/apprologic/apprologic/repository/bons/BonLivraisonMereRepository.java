package mg.apprologic.apprologic.repository.bons;

import mg.apprologic.apprologic.model.bons.BonLivraisonMere;
import mg.apprologic.apprologic.model.stock.StockMere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BonLivraisonMereRepository extends JpaRepository<BonLivraisonMere,Integer> {


}
