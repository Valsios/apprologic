package mg.apprologic.apprologic.repository.article;


import mg.apprologic.apprologic.model.article.CentreBudgetaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CentreBudgetaireRepository extends JpaRepository<CentreBudgetaire,Integer> {
}
