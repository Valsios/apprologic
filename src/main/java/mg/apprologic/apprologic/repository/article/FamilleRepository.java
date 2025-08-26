package mg.apprologic.apprologic.repository.article;

import mg.apprologic.apprologic.model.article.Famille;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FamilleRepository extends JpaRepository<Famille,Integer> {
}
