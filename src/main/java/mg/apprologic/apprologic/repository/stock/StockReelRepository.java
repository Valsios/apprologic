package mg.apprologic.apprologic.repository.stock;

import mg.apprologic.apprologic.model.article.Article;
import mg.apprologic.apprologic.model.stock.StockReel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockReelRepository extends JpaRepository<StockReel,Integer> {
}
