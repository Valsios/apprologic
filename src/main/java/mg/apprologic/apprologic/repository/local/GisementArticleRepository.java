package mg.apprologic.apprologic.repository.local;

import mg.apprologic.apprologic.model.local.GisementArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GisementArticleRepository extends JpaRepository<GisementArticle,Integer> {
}
