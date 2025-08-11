package mg.apprologic.apprologic.model.local;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import mg.apprologic.apprologic.model.article.Article;
import mg.apprologic.apprologic.model.stock.StockFille;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Entity
@Table(name = "gisement_stock_fille")
public class GisementStockFille {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_gsf")
    private Integer idGsf;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_article", nullable = false)
    private Article article;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_gisement", nullable = false)
    private ExistantGisement gisement;

    @Column(name = "quantite_in")
    private Double quantite_in;

    @Column(name = "quantite_out")
    private Double quantite_out;

    @Column(name = "hors_local")
    private Double hors_local;

    @Column(name = "sortie_hors_local")
    private Double sortie_hors_local;

    @Column(name = "date_mouvement")
    private LocalDateTime dateMouvement;

    @Transient
    @JsonProperty("capaciteMaxUnnitaire")
    private Double capaciteMaxUnnitaire;
    // Getters et Setters

    public Double tauxLibre()
    {
        Double occupation = (this.getQuantite_in()-this.getQuantite_out())/this.getCapaciteMaxUnnitaire();
        return 100 - (occupation*100);
    }

    public Double absolu(){
        return Math.abs(tauxLibre()- 100);
    }

    @JsonProperty("capaciteMaxUnnitaire")
    public Double getCapaciteMaxUnnitaire() {
        return capaciteMaxUnnitaire;
    }

    public void setCapaciteMaxUnnitaire(Double capaciteMaxUnnitaire) {
        this.capaciteMaxUnnitaire = capaciteMaxUnnitaire;
    }

    public Integer getIdGsf() {
        return idGsf;
    }

    public void setIdGsf(Integer idGsf) {
        this.idGsf = idGsf;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public ExistantGisement getGisement() {
        return gisement;
    }

    public void setGisement(ExistantGisement gisement) {
        this.gisement = gisement;
    }

    public Double getQuantite_in() {
        return quantite_in;
    }

    public void setQuantite_in(Double quantite_in) {
        this.quantite_in = quantite_in;
    }

    public Double getQuantite_out() {
        return quantite_out;
    }

    public void setQuantite_out(Double quantite_out) {
        this.quantite_out = quantite_out;
    }

    public LocalDateTime getDateMouvement() {
        return dateMouvement;
    }

    public void setDateMouvement(LocalDateTime dateMouvement) {
        this.dateMouvement = dateMouvement;
    }

    public Double getHors_local() {
        return hors_local;
    }

    public void setHors_local(Double hors_local) {
        this.hors_local = hors_local;
    }

    public Double getSortie_hors_local() {
        return sortie_hors_local;
    }

    public void setSortie_hors_local(Double sortie_hors_local) {
        this.sortie_hors_local = sortie_hors_local;
    }
}

