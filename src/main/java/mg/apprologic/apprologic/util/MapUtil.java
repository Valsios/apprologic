package mg.apprologic.apprologic.util;

import mg.apprologic.apprologic.model.article.Article;
import mg.apprologic.apprologic.model.consommateur.Consommateur;
import mg.apprologic.apprologic.model.fournisseur.Fournisseur;

import java.util.HashMap;
import java.util.Map;

public class MapUtil {

    public static HashMap<String,Double> transformKey(HashMap<?,Double> data)
    {
        HashMap<String,Double> toReturn = new HashMap<>();
        for (Map.Entry<?, Double> entry : data.entrySet())
        {
            Object key = entry.getKey();
            if (key instanceof Fournisseur)
            {
                Fournisseur fournisseur = (Fournisseur) key;
                toReturn.put(fournisseur.getNom(),entry.getValue());
            }
            else if (key instanceof Consommateur)
            {
                Consommateur consommateur = (Consommateur) key;
                toReturn.put(consommateur.getDescription(),entry.getValue());
            }
            else if (key instanceof Article)
            {
                Article article = (Article) key;
                toReturn.put(article.getCodeArticle()+"-"+article.getDesignation(),entry.getValue());

            }
        }
        return toReturn;
    }
}
