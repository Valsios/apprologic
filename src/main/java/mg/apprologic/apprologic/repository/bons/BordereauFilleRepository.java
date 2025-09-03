package mg.apprologic.apprologic.repository.bons;

import mg.apprologic.apprologic.model.article.Article;
import mg.apprologic.apprologic.model.bons.BordereauFille;
import mg.apprologic.apprologic.model.bons.BordereauMere;
import mg.apprologic.apprologic.model.consommateur.Consommateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.w3c.dom.ls.LSException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BordereauFilleRepository extends JpaRepository<BordereauFille,Integer> {

    @Query("SELECT b FROM BordereauFille b WHERE b.demandeFille.article = :article AND b.bordereauMere.dateBordereau between :minus3 AND :present")
    List<BordereauFille> getByArticleAndDateBetween(Article article, LocalDateTime  minus3,LocalDateTime present);


    //pour le taux de satisfaction des demandes et departement plus en unité consommateur
    @Query("SELECT b FROM BordereauFille b WHERE YEAR(b.bordereauMere.dateBordereau) = :year AND b.demandeFille.article = :article")
    public List<BordereauFille> getBordereauFilleByArticleAndYear(Article article,Integer year);

    //Sortie annuel et mensuel
    @Query("SELECT b FROM BordereauFille b WHERE YEAR(b.bordereauMere.dateBordereau) = :year AND b.demandeFille.demandeMere.consommateur = :consommateur AND (:month IS NULL OR MONTH(b.bordereauMere.dateBordereau)=:month )")
    public List<BordereauFille> getBordereauFilleByConsommateurAndYearAndMonth(Consommateur consommateur, Integer year, Integer month);

    public List<BordereauFille> getBordereauFilleByBordereauMere(BordereauMere bordereauMere);

}
