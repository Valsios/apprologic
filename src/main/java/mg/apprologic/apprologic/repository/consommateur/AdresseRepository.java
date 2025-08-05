package mg.apprologic.apprologic.repository.consommateur;

import mg.apprologic.apprologic.model.consommateur.Adresse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdresseRepository extends JpaRepository<Adresse,Integer> {
}
