package mg.apprologic.apprologic.repository.stock;

import mg.apprologic.apprologic.model.article.Article;
import mg.apprologic.apprologic.model.stock.StockFille;
import mg.apprologic.apprologic.model.stock.StockMere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface StockFilleRepository extends JpaRepository<StockFille,Integer> {

    @Query("SELECT s FROM StockFille s WHERE year (s.stockMere.dateMouvement) = :year AND s.article = :article")
    List<StockFille> getStockFilleByArticleAndYear(Article article ,Integer year);

    @Query("SELECT s.article, " +
            "SUM(CASE WHEN sm.dateMouvement <= COALESCE(:date, CURRENT_TIMESTAMP) THEN s.entree ELSE 0.0 END) AS total_entree, " +
            "SUM(CASE WHEN sm.dateMouvement <= COALESCE(:date, CURRENT_TIMESTAMP) THEN s.sortie ELSE 0.0 END) AS total_sortie, " +
            "MAX(sm.dateMouvement) as last_date " +
            "FROM StockFille s JOIN s.stockMere sm " +
            "WHERE (:designation IS NULL OR " +
            "     (LOWER(TRIM(CAST(s.article.designation AS string))) LIKE LOWER(CONCAT('%', TRIM(CAST(:designation AS string)), '%')) OR " +
            "     LOWER(TRIM(CAST(s.article.codeArticle AS string))) LIKE LOWER(CONCAT('%', TRIM(CAST(:designation AS string)), '%')))) " +
            "GROUP BY s.article")
    List<Object[]> findStockDateByDesignationIgnoreCase(
            @Param("date") LocalDateTime date,
            @Param("designation") String designation);

    @Query("SELECT s.article, " +
            "SUM(CASE WHEN sm.dateMouvement <= COALESCE(:date, CURRENT_TIMESTAMP) THEN s.entree ELSE 0.0 END) AS total_entree, " +
            "SUM(CASE WHEN sm.dateMouvement <= COALESCE(:date, CURRENT_TIMESTAMP) THEN s.sortie ELSE 0.0 END) AS total_sortie, " +
            "MAX(sm.dateMouvement) as last_date " +
            "FROM StockFille s JOIN s.stockMere sm WHERE s.article = :article GROUP BY s.article ")
    List<Object[]> findStockFilleByArticle(LocalDateTime date, Article article);

    @Query("SELECT s.article, " +
            "SUM(CASE WHEN sm.dateMouvement <= COALESCE(:date, CURRENT_TIMESTAMP) THEN s.entree ELSE 0.0 END) AS total_entree, " +
            "SUM(CASE WHEN sm.dateMouvement <= COALESCE(:date, CURRENT_TIMESTAMP) THEN s.sortie ELSE 0.0 END) AS total_sortie, " +
            "MAX(sm.dateMouvement) as last_date " +
            "FROM StockFille s JOIN s.stockMere sm WHERE s.article = :article AND (sm.bonLivraisonMere IS NOT NULL OR sm.demandeMere IS NOT NULL )GROUP BY s.article ")
    List<Object[]> findStockFilleByArticleTheorique(LocalDateTime date, Article article);


    List<StockFille> getStockFilleByStockMere(StockMere stockMere);
}
