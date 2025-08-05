package mg.apprologic.apprologic.model.bons;
import jakarta.persistence.*;
import mg.apprologic.apprologic.exception.ExceptionValueNumber;

@Entity
@Table(name = "bordereau_fille")
public class BordereauFille {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bordereau_fille")
    private Integer idBordereauFille;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_bordereau_mere", nullable = false)
    private BordereauMere bordereauMere;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_demande_fille", nullable = false)
    private DemandeFille demandeFille;

    @Column(name = "prix_unitaire", nullable = false)
    private Double prixUnitaire;

    @Column(name = "quantite_sortie", nullable = false)
    private Double quantiteSortie;

    // Getters et Setters


    public void setPrixUnitaire(String prixUnitaire) throws ExceptionValueNumber {

        try {
            this.prixUnitaire = Double.parseDouble(prixUnitaire);
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



    public void setQuantiteSortie(String quantiteSortie) throws ExceptionValueNumber {

        try {
            this.quantiteSortie = Double.parseDouble(quantiteSortie);
            if (this.quantiteSortie<1 || this.quantiteSortie>this.getDemandeFille().getQuantite())
            {
                throw new ExceptionValueNumber("Quantité sortie invalide.");
            }
        }
        catch (Exception e)
        {
            throw new ExceptionValueNumber(e.getMessage());
        }

    }

    public Integer getIdBordereauFille() {
        return idBordereauFille;
    }

    public void setIdBordereauFille(Integer idBordereauFille) {
        this.idBordereauFille = idBordereauFille;
    }

    public BordereauMere getBordereauMere() {
        return bordereauMere;
    }

    public void setBordereauMere(BordereauMere bordereauMere) {
        this.bordereauMere = bordereauMere;
    }

    public DemandeFille getDemandeFille() {
        return demandeFille;
    }

    public void setDemandeFille(DemandeFille demandeFille) {
        this.demandeFille = demandeFille;
    }

    public Double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(Double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public Double getQuantiteSortie() {
        return quantiteSortie;
    }

    public void setQuantiteSortie(Double quantiteSortie) {
        this.quantiteSortie = quantiteSortie;
    }

    public Double geTotal()
    {
        return this.quantiteSortie*this.prixUnitaire;
    }
}

