package mg.apprologic.apprologic.repository.bons;

import mg.apprologic.apprologic.model.bons.BordereauMere;
import mg.apprologic.apprologic.model.consommateur.Consommateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BordereauMereRepository extends JpaRepository<BordereauMere,Integer> {

    @Query("SELECT b FROM BordereauMere b where b.demandeMere.consommateur = :consommateur")
    public List<BordereauMere> getBordereauMereByConsommateur(Consommateur consommateur);

    @Query("SELECT b FROM BordereauMere b WHERE b.demandeMere.idDemandeMere = :idDemandeMere")
    public BordereauMere getBordereauMereByIdDemandeMere(Integer idDemandeMere);
}
