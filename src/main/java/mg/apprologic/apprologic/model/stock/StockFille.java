package mg.apprologic.apprologic.model.stock;

import jakarta.persistence.*;
import mg.apprologic.apprologic.model.article.Article;
import mg.apprologic.apprologic.model.article.Udm;

import java.time.LocalDateTime;

@Entity
@Table(name = "stock_fille")
public class StockFille {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_stock_fille")
    private Integer idStockFille;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_stock_mere", nullable = false)
    private StockMere stockMere;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_article", nullable = false)
    private Article article;

    @Column(name = "sortie", nullable = false)
    private Double sortie;
    @Column(name = "entree", nullable = false)
    private Double entree;
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_udm", nullable = false)
    private Udm udm;

    @Transient
    private Double total_entree;

    @Transient
    private Double total_sortie;

    @Transient
    private Double stock_date;

    @Transient
    private LocalDateTime last_date;


    // Getters et Setters


    public Double getTotal_entree() {
        return total_entree;
    }

    public void setTotal_entree(Double total_entree) {
        this.total_entree = total_entree;
    }

    public Double getTotal_sortie() {
        return total_sortie;
    }

    public void setTotal_sortie(Double total_sortie) {
        this.total_sortie = total_sortie;
    }

    public Double getStock_date() {
        return stock_date;
    }

    public void setStock_date(Double stock_date) {
        this.stock_date = stock_date;
    }

    public LocalDateTime getLast_date() {
        return last_date;
    }

    public void setLast_date(LocalDateTime last_date) {
        this.last_date = last_date;
    }

    public Integer getIdStockFille() {
        return idStockFille;
    }

    public void setIdStockFille(Integer idStockFille) {
        this.idStockFille = idStockFille;
    }

    public StockMere getStockMere() {
        return stockMere;
    }

    public void setStockMere(StockMere stockMere) {
        this.stockMere = stockMere;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Double getSortie() {
        return sortie;
    }

    public void setSortie(Double sortie) {
        this.sortie = sortie;
    }

    public Double getEntree() {
        return entree;
    }

    public void setEntree(Double entree) {
        this.entree = entree;
    }

    public Udm getUdm() {
        return udm;
    }

    public void setUdm(Udm udm) {
        this.udm = udm;
    }
}
