package mg.apprologic.apprologic.repository.bons;

import mg.apprologic.apprologic.model.bons.BordereauMere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BordereauMereRepository extends JpaRepository<BordereauMere,Integer> {
}
