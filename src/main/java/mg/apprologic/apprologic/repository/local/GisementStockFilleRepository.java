package mg.apprologic.apprologic.repository.local;

import mg.apprologic.apprologic.model.article.Article;
import mg.apprologic.apprologic.model.local.ExistantGisement;
import mg.apprologic.apprologic.model.local.GisementArticle;
import mg.apprologic.apprologic.model.local.GisementStockFille;
import mg.apprologic.apprologic.model.local.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.table.TableRowSorter;
import java.util.List;
import java.util.Locale;

@Repository
public interface GisementStockFilleRepository extends JpaRepository<GisementStockFille,Integer> {

    @Query(" SELECT ga.gisement, COALESCE( SUM(g.quantite_in),0) ,COALESCE(SUM(g.quantite_out),0) FROM GisementArticle ga LEFT JOIN GisementStockFille g ON ga.gisement = g.gisement WHERE ga.gisement = :existantGisement GROUP BY ga.gisement")
    public List<Object[]> getOccupationGisement(ExistantGisement existantGisement);

    @Query(" SELECT ga.article ,ga.gisement, COALESCE( SUM(g.quantite_in),0) ,COALESCE(SUM(g.quantite_out),0),ga.capaciteMaxUnitaire FROM GisementArticle ga LEFT JOIN GisementStockFille g ON ga.gisement = g.gisement WHERE (:local IS NULL OR ga.gisement.local = :local) AND (:article IS NULL OR ga.article = :article) GROUP BY ga.gisement,ga.article,ga.capaciteMaxUnitaire")
    public List<Object[]> getAllOccupationGisementByLocale(Local local,Article article);

    public List<GisementStockFille> findGisementStockFilleByArticleOrderByDateMouvementAsc(Article article);
}
