package mg.apprologic.apprologic.model.stock;

import mg.apprologic.apprologic.model.article.Article;

import java.time.LocalDateTime;

public class AnomalieStock {

    Article article;
    Double stock_theorique;
    Double stock_physique;

    LocalDateTime dateInventaire;
    String description;



    public boolean isColor_check() {
        return this.getStock_physique()>this.getStock_theorique();
    }



    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Double getStock_theorique() {
        return stock_theorique;
    }

    public void setStock_theorique(Double stock_theorique) {
        this.stock_theorique = stock_theorique;
    }

    public Double getStock_physique() {
        return stock_physique;
    }

    public void setStock_physique(Double stock_physique) {
        this.stock_physique = stock_physique;
    }

    public LocalDateTime getDateInventaire() {
        return dateInventaire;
    }

    public void setDateInventaire(LocalDateTime dateInventaire) {
        this.dateInventaire = dateInventaire;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
