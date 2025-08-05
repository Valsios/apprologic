package mg.apprologic.apprologic.model.stock;

import jakarta.persistence.*;
import mg.apprologic.apprologic.model.article.Article;

import java.time.LocalDateTime;

@Entity
public class ReapproNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Article article;

    private Double quantiteRecommandee;

    private LocalDateTime dateCreation;

    private boolean lue = false;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Double getQuantiteRecommandee() {
        return quantiteRecommandee;
    }

    public void setQuantiteRecommandee(Double quantiteRecommandee) {
        this.quantiteRecommandee = quantiteRecommandee;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public boolean isLue() {
        return lue;
    }

    public void setLue(boolean lue) {
        this.lue = lue;
    }
}