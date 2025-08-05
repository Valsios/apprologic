package mg.apprologic.apprologic.model.local;
import jakarta.persistence.*;
import mg.apprologic.apprologic.model.stock.StockFille;

@Entity
@Table(name = "gisement_stock_fille")
public class GisementStockFille {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_gsf")
    private Integer idGsf;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_stock_fille", nullable = false)
    private StockFille stockFille;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_gisement", nullable = false)
    private ExistantGisement gisement;

    // Getters et Setters

    public Integer getIdGsf() {
        return idGsf;
    }

    public void setIdGsf(Integer idGsf) {
        this.idGsf = idGsf;
    }

    public StockFille getStockFille() {
        return stockFille;
    }

    public void setStockFille(StockFille stockFille) {
        this.stockFille = stockFille;
    }

    public ExistantGisement getGisement() {
        return gisement;
    }

    public void setGisement(ExistantGisement gisement) {
        this.gisement = gisement;
    }
}

