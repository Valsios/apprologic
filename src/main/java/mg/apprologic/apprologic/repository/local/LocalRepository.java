package mg.apprologic.apprologic.repository.local;

import mg.apprologic.apprologic.model.local.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LocalRepository extends JpaRepository<Local,Integer> {


    public Local getLocalByDesignation (String designation);
}
