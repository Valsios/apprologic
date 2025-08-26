package mg.apprologic.apprologic.repository.bons;

import mg.apprologic.apprologic.model.article.Article;
import mg.apprologic.apprologic.model.bons.BonLivraisonFille;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BonLivraisonFilleRepository extends JpaRepository<BonLivraisonFille,Integer> {
    public List<BonLivraisonFille> getBonLivraisonFilleByArticle(Article article);


    @Query("SELECT b FROM BonLivraisonFille b WHERE b.article = :article AND year (b.bonLivraisonMere.dateReception) = :year")
    public List<BonLivraisonFille> getBonLivraisonFilleByArticleAndYear(Article article,Integer year);
}
