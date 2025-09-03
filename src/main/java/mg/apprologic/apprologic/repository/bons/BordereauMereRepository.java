package mg.apprologic.apprologic.repository.bons;

import mg.apprologic.apprologic.model.bons.BonLivraisonMere;
import mg.apprologic.apprologic.model.bons.BordereauMere;
import mg.apprologic.apprologic.model.consommateur.Consommateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BordereauMereRepository extends JpaRepository<BordereauMere,Integer> {

    @Query("SELECT b FROM BordereauMere b where b.demandeMere.consommateur = :consommateur")
    public List<BordereauMere> getBordereauMereByConsommateur(Consommateur consommateur);


    //global analyse
    @Query("SELECT b FROM BordereauMere b where year (b.dateBordereau) = :year")
    public List<BordereauMere> getBordereauMereByYear(Integer year);

    @Query("SELECT b FROM BordereauMere b WHERE b.demandeMere.idDemandeMere = :idDemandeMere")
    public BordereauMere getBordereauMereByIdDemandeMere(Integer idDemandeMere);

    @Query(value = "SELECT bd.*, dm.id_demande_mere as dm_id_demande_mere " +
            "FROM bordereau_mere bd " +
            "JOIN demande_mere dm ON bd.id_demande_mere = dm.id_demande_mere " +
            "WHERE (:consommateurId = '' OR :consommateurId IS NULL OR dm.id_consommateur = CAST(NULLIF(:consommateurId, '') AS INTEGER)) AND " +
            "(:debut = '' OR :debut IS NULL OR bd.date_bordereau >= CAST(NULLIF(:debut, '') AS TIMESTAMP)) AND " +
            "(:fin = '' OR :fin IS NULL OR bd.date_bordereau <= CAST(NULLIF(:fin, '') AS TIMESTAMP))",
            nativeQuery = true)
    List<BordereauMere> getByConsommateurDateNative(
            @Param("consommateurId") String consommateurId,
            @Param("debut") String debut,
            @Param("fin") String fin);
}
