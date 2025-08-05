package mg.apprologic.apprologic.repository.article;

import mg.apprologic.apprologic.model.article.Devise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviseRepository extends JpaRepository<Devise,Integer> {
}
