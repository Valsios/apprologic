package mg.apprologic.apprologic.repository.consommateur;

import mg.apprologic.apprologic.model.consommateur.Consommateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsommateurRepository extends JpaRepository<Consommateur,Integer> {

    public List<Consommateur> findConsommateurByMereIsNull();

    public List<Consommateur> findConsommateurByMere(Consommateur consommateur$);
}
