package mg.apprologic.apprologic.services.article;

import mg.apprologic.apprologic.model.article.Article;
import mg.apprologic.apprologic.repository.article.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {

    @Autowired
    ArticleRepository articleRepository;

    public List<Article> getAll()
    {
        return articleRepository.findAll();
    }

    public List<Article> getByDesignation(String designation) {
        return articleRepository.getArticleByDesignationContainingIgnoreCase(designation);
    }
    public List<Article> getByCodeArticle(String codeArticle) {
        return articleRepository.getArticleByCodeArticleContainingIgnoreCase(codeArticle);
    }

    public List<Article> getByContaining(String text)
    {
        return articleRepository.findByDesignationContainingIgnoreCaseOrCodeArticleContaining(text,text);
    }
    public Article getById(Integer id)
    {
        return articleRepository.getByIdArticle(id);
    }

    public void save(Article article)
    {
        articleRepository.save(article);
    }


}
