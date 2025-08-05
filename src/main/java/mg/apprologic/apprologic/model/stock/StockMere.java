package mg.apprologic.apprologic.model.stock;
import jakarta.persistence.*;
import mg.apprologic.apprologic.model.bons.DemandeMere;
import mg.apprologic.apprologic.model.bons.BonLivraisonMere;

import java.time.LocalDateTime;

@Entity
@Table(name = "stock_mere")
public class StockMere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_stock_mere")
    private Integer idStockMere;

    @Column(name = "date_mouvement", nullable = false)
    private LocalDateTime dateMouvement;

    @ManyToOne(optional = true)
    @JoinColumn(name = "id_demande_mere")
    private DemandeMere demandeMere;

    @ManyToOne(optional = true)
    @JoinColumn(name = "id_bl_mere")
    private BonLivraisonMere bonLivraisonMere;

    @Column(name = "description", nullable = false)
    private String description;

    // Getters et Setters

    public DemandeMere getDemandeMere() {
        return demandeMere;
    }

    public void setDemandeMere(DemandeMere demandeMere) {
        this.demandeMere = demandeMere;
    }

    public Integer getIdStockMere() {
        return idStockMere;
    }

    public void setIdStockMere(Integer idStockMere) {
        this.idStockMere = idStockMere;
    }

    public LocalDateTime getDateMouvement() {
        return dateMouvement;
    }

    public void setDateMouvement(LocalDateTime dateMouvement) {
        this.dateMouvement = dateMouvement;
    }


    public BonLivraisonMere getBonLivraisonMere() {
        return bonLivraisonMere;
    }

    public void setBonLivraisonMere(BonLivraisonMere bonLivraisonMere) {
        this.bonLivraisonMere = bonLivraisonMere;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
