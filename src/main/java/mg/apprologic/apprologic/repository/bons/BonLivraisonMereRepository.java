package mg.apprologic.apprologic.repository.bons;

import mg.apprologic.apprologic.model.bons.BonLivraisonMere;
import mg.apprologic.apprologic.model.fournisseur.Fournisseur;
import mg.apprologic.apprologic.model.stock.StockMere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BonLivraisonMereRepository extends JpaRepository<BonLivraisonMere,Integer> {

    @Query("SELECT b FROM BonLivraisonMere b WHERE YEAR(b.dateReception) = :year")
    public List<BonLivraisonMere> getBonLivraisonMereByYear(Integer year);


    @Query(value = "SELECT * FROM bon_livraison_mere blm WHERE " +
            "(:fournisseurId = '' OR :fournisseurId IS NULL OR blm.id_fournisseur = CAST(NULLIF(:fournisseurId, '') AS INTEGER)) AND " +
            "(:debut = '' OR :debut IS NULL OR blm.date_reception >= CAST(NULLIF(:debut, '') AS TIMESTAMP)) AND " +
            "(:fin = '' OR :fin IS NULL OR blm.date_reception <= CAST(NULLIF(:fin, '') AS TIMESTAMP))",
            nativeQuery = true)
    List<BonLivraisonMere> getByFournisseurDateNative(
            @Param("fournisseurId") String fournisseurId,
            @Param("debut") String debut,
            @Param("fin") String fin);

}
