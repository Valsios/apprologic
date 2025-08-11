package mg.apprologic.apprologic.model.local;
import jakarta.persistence.*;
import mg.apprologic.apprologic.model.article.Article;

@Entity
@Table(name = "gisement_article")
public class GisementArticle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_gisement_article")
    private Integer idGisementArticle;

    @ManyToOne
    @JoinColumn(name = "id_article")
    private Article article;

    @ManyToOne
    @JoinColumn(name = "id_gisement")
    private ExistantGisement gisement;

    @Column(name = "capacite_max_unitaire", nullable = false)
    private Double capaciteMaxUnitaire;

    // Getters et Setters

    public Integer getIdGisementArticle() {
        return idGisementArticle;
    }

    public void setIdGisementArticle(Integer idGisementArticle) {
        this.idGisementArticle = idGisementArticle;
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

    public Double getCapaciteMaxUnitaire() {
        return capaciteMaxUnitaire;
    }

    public void setCapaciteMaxUnitaire(Double capaciteMaxUnitaire) {
        this.capaciteMaxUnitaire = capaciteMaxUnitaire;
    }
}

