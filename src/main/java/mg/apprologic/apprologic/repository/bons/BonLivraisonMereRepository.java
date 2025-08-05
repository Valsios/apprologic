package mg.apprologic.apprologic.repository.bons;

import mg.apprologic.apprologic.model.bons.BonLivraisonMere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BonLivraisonMereRepository extends JpaRepository<BonLivraisonMere,Integer> {
}
