package mg.apprologic.apprologic.model.stock;

import jakarta.persistence.*;
import mg.apprologic.apprologic.model.article.Article;

import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_reel")
public class StockReel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_stock_reel")
    private Integer idStockReel;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_article", nullable = false)
    private Article article;

    @Column(name = "date_inventaire", nullable = false)
    private LocalDateTime dateInventaire;

    @Column(name = "stock_reel", nullable = false)
    private Double stockReel;

    // Getters et Setters


    public Integer getIdStockReel() {
        return idStockReel;
    }

    public void setIdStockReel(Integer idStockReel) {
        this.idStockReel = idStockReel;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public LocalDateTime getDateInventaire() {
        return dateInventaire;
    }

    public void setDateInventaire(LocalDateTime dateInventaire) {
        this.dateInventaire = dateInventaire;
    }

    public Double getStockReel() {
        return stockReel;
    }

    public void setStockReel(Double stockReel) {
        this.stockReel = stockReel;
    }
}
