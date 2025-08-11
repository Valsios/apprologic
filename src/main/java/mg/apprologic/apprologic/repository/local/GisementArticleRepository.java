package mg.apprologic.apprologic.repository.local;

import mg.apprologic.apprologic.model.article.Article;
import mg.apprologic.apprologic.model.local.ExistantGisement;
import mg.apprologic.apprologic.model.local.GisementArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GisementArticleRepository extends JpaRepository<GisementArticle,Integer> {

public List<GisementArticle> getGisementArticleByArticle(Article article);

public GisementArticle getGisementArticleByGisement(ExistantGisement existantGisement);


}
