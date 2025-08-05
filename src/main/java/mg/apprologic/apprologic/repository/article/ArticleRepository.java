package mg.apprologic.apprologic.repository.article;

import mg.apprologic.apprologic.model.article.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article,Integer> {

    public Article getByIdArticle(Integer id);

    public List<Article> getArticleByDesignationContainingIgnoreCase(String designation);
    public List<Article> getArticleByCodeArticleContainingIgnoreCase(String codeArticle);

    List<Article> findByDesignationContainingIgnoreCaseOrCodeArticleContaining(String designation, String codeArticle);
}
