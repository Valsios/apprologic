package mg.apprologic.apprologic.model.bons;
import jakarta.persistence.*;
import mg.apprologic.apprologic.exception.ExceptionValueNumber;
import mg.apprologic.apprologic.model.article.Article;
import mg.apprologic.apprologic.model.article.Devise;
import mg.apprologic.apprologic.model.article.Udm;

@Entity
@Table(name = "bon_livraison_fille")
public class BonLivraisonFille {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bl_fille")
    private Integer idBlFille;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_bl_mere", nullable = false)
    private BonLivraisonMere bonLivraisonMere;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_article", nullable = false)
    private Article article;

    @Column(name = "quantite_demande", nullable = false)
    private Double quantite_demande;

    @Column(name = "quantite_recu", nullable = false)
    private Double quantite_recu;

    @Column(name = "prix_unitaire", nullable = false)
    private Double prixUnitaire;

    // Getters et Setters

    public Integer getIdBlFille() {
        return idBlFille;
    }

    public void setIdBlFille(Integer idBlFille) {
        this.idBlFille = idBlFille;
    }

    public BonLivraisonMere getBonLivraisonMere() {
        return bonLivraisonMere;
    }

    public void setBonLivraisonMere(BonLivraisonMere bonLivraisonMere) {
        this.bonLivraisonMere = bonLivraisonMere;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Double getQuantite_demande() {
        return quantite_demande;
    }

    public void setQuantite_demande(Double quantite_demande) {
        this.quantite_demande = quantite_demande;
    }

    public Double getQuantite_recu() {
        return quantite_recu;
    }

    public void setQuantite_recu(Double quantite_recu) {
        this.quantite_recu = quantite_recu;
    }

    public Double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(Double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public void setPrixUnitaire(String s) throws ExceptionValueNumber {
        try {
            this.setPrixUnitaire(Double.parseDouble(s));
            if (this.prixUnitaire<0)
            {
                throw new ExceptionValueNumber("Prix unitaire invalide.");
            }
        }
        catch (Exception e)
        {
            throw new ExceptionValueNumber(e.getMessage());
        }
    }

    public void setQuantite_demande(String s) throws ExceptionValueNumber {
        try {
            this.setQuantite_demande(Double.parseDouble(s));
            if (this.quantite_demande<1)
            {
                throw new ExceptionValueNumber("Quantité demandé invalide.");
            }
        }
        catch (Exception e)
        {
            throw new ExceptionValueNumber(e.getMessage());
        }
    }

    public void setQuantite_recu(String s) throws ExceptionValueNumber {
        try {
            this.setQuantite_recu(Double.parseDouble(s));
            if (this.quantite_recu<0  || this.quantite_recu>this.getQuantite_demande())
            {
                throw new ExceptionValueNumber("Quantité reçu invalide.");
            }
        }
        catch (Exception e)
        {
            throw new ExceptionValueNumber(e.getMessage());
        }
    }
}

