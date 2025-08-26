package mg.apprologic.apprologic.services.article;

import mg.apprologic.apprologic.model.article.Article;
import mg.apprologic.apprologic.model.bons.BonLivraisonFille;
import mg.apprologic.apprologic.repository.article.ArticleRepository;
import mg.apprologic.apprologic.services.bons.BonLivraisonFilleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    BonLivraisonFilleService bonLivraisonFilleService;
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

    public Double prixPondereArticle(Article article)
    {
        Double result = 0.0;
        Double quantité = 0.0;
        List<BonLivraisonFille> bonLivraisonFilleList = bonLivraisonFilleService.getLivraisonByArticle(article);
        for (BonLivraisonFille bon: bonLivraisonFilleList)
        {
            result += bon.getPrixUnitaire()*bon.getQuantite_recu();
            quantité += bon.getQuantite_recu();
        }
        return result/quantité;
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
