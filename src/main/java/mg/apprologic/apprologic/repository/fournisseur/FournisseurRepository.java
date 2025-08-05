package mg.apprologic.apprologic.repository.fournisseur;

import mg.apprologic.apprologic.model.fournisseur.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FournisseurRepository extends JpaRepository<Fournisseur,Integer> {

}
