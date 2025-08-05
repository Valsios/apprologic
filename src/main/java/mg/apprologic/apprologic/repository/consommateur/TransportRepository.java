package mg.apprologic.apprologic.repository.consommateur;

import mg.apprologic.apprologic.model.consommateur.Transport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TransportRepository extends JpaRepository<Transport,Integer> {
}
