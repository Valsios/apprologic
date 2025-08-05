package mg.apprologic.apprologic.repository.bons;

import mg.apprologic.apprologic.model.bons.BonLivraisonFille;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BonLivraisonFilleRepository extends JpaRepository<BonLivraisonFille,Integer> {
}
