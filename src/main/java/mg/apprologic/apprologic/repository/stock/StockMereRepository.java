package mg.apprologic.apprologic.repository.stock;

import mg.apprologic.apprologic.model.stock.StockMere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockMereRepository extends JpaRepository<StockMere,Integer> {

    public List<StockMere> getStockMereByDemandeMereIsNullAndBonLivraisonMereIsNull();
}
