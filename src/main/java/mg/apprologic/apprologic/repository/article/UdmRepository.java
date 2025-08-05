package mg.apprologic.apprologic.repository.article;

import mg.apprologic.apprologic.model.article.Udm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UdmRepository extends JpaRepository<Udm,Integer> {
}
