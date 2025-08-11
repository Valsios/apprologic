package mg.apprologic.apprologic.model.stock;

import mg.apprologic.apprologic.model.article.Article;

import java.time.LocalDateTime;

public class AnomalieStock {

    Article article;
    Double stock_theorique;
    Double stock_physique;

    LocalDateTime dateInventaire;
    String description;



    public int isColor_check() {
        if (this.getStock_physique()>this.getStock_theorique())
        {
            this.setDescription("Anomalie stock : excedentaire.");
            return 1;
        } else if (this.getStock_theorique()>this.getStock_physique()) {

            this.setDescription("Anomalie stock : deficit.");
            return -1;
        }
        else {
            return 0;
        }
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
