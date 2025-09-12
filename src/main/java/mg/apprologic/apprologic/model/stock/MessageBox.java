package mg.apprologic.apprologic.model.stock;


import mg.apprologic.apprologic.model.article.Article;

import java.time.LocalDateTime;

public class MessageBox {

    public Article article;


    //soit epuisement soit seuil
    public String type_message;


    public Double enStock;

    public LocalDateTime dernierMouvement;



    public void setEnStock(Double enStock) {
        this.enStock = enStock;
        if (this.enStock<=0)
        {
            this.setType_message("epuisement");
        }
        else if(this.enStock<=this.article.getSeuilMin()){
            this.setType_message("seuil");
        }

    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public String getType_message() {
        return type_message;
    }

    public void setType_message(String type_message) {
        this.type_message = type_message;
    }

    public Double getEnStock() {
        return enStock;
    }

    public LocalDateTime getDernierMouvement() {
        return dernierMouvement;
    }

    public void setDernierMouvement(LocalDateTime dernierMouvement) {
        this.dernierMouvement = dernierMouvement;
    }
}
