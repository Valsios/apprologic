package mg.apprologic.apprologic.model.bons;
import jakarta.persistence.*;
import mg.apprologic.apprologic.exception.ExceptionValueNumber;
import mg.apprologic.apprologic.model.article.Article;
import mg.apprologic.apprologic.model.article.Udm;

@Entity
@Table(name = "demande_fille")
public class DemandeFille {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_demande_fille")
    private Integer idDemandeFille;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_demande_mere", nullable = false)
    private DemandeMere demandeMere;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_article", nullable = false)
    private Article article;

    @Column(name = "quantite", nullable = false)
    private Double quantite;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_udm", nullable = false)
    private Udm udm;

    // Getters et Setters


    public Integer getIdDemandeFille() {
        return idDemandeFille;
    }

    public void setIdDemandeFille(Integer idDemandeFille) {
        this.idDemandeFille = idDemandeFille;
    }

    public DemandeMere getDemandeMere() {
        return demandeMere;
    }

    public void setDemandeMere(DemandeMere demandeMere) {
        this.demandeMere = demandeMere;
    }



    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) throws Exception {
        if (article == null)
        {
            throw new Exception("Article null.");
        }
        this.article = article;
    }

    public Double getQuantite() {
        return quantite;
    }

    public void setQuantite(Double quantite) {
        this.quantite = quantite;
    }

    public void setQuantite(String quantite)throws Exception {

        try {
            this.quantite = Double.parseDouble(quantite);
            if (this.quantite<1)
            {
                throw new ExceptionValueNumber("Quantité invalide");
            }
        }
        catch (Exception e)
        {
            throw new ExceptionValueNumber(e.getMessage());
        }
    }


    public Udm getUdm() {
        return udm;
    }

    public void setUdm(Udm udm) {
        this.udm = udm;
    }

}

