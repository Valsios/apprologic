package mg.apprologic.apprologic.services.local;

import mg.apprologic.apprologic.model.article.Article;
import mg.apprologic.apprologic.model.local.ExistantGisement;
import mg.apprologic.apprologic.model.local.GisementArticle;
import mg.apprologic.apprologic.repository.local.GisementArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GisementArticleService {

    @Autowired
    GisementArticleRepository gisementArticleRepository;
    public List<ExistantGisement> getGisementByArticle(Article article)
    {
        List<GisementArticle> gisementArticleList = gisementArticleRepository.getGisementArticleByArticle(article);
        List<ExistantGisement> existantGisementList = new ArrayList<>();
        for (GisementArticle gisementArticle : gisementArticleList)
        {
            existantGisementList.add(gisementArticle.getGisement());
        }
        return existantGisementList;
    }

    public GisementArticle capaciteGisement(ExistantGisement existantGisement)
    {
        return gisementArticleRepository.getGisementArticleByGisement(existantGisement);
    }

    public GisementArticle getBYGisement(ExistantGisement existantGisement)
    {
        return gisementArticleRepository.getGisementArticleByGisement(existantGisement);
    }

    public void save(GisementArticle e)
    {
        gisementArticleRepository.save(e);
    }

}
