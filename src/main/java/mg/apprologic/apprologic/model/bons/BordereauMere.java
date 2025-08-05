package mg.apprologic.apprologic.model.bons;


import jakarta.persistence.*;
import mg.apprologic.apprologic.exception.ExceptionDate;
import mg.apprologic.apprologic.model.article.Devise;
import mg.apprologic.apprologic.model.consommateur.Transport;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bordereau_mere")
public class BordereauMere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bordereau_mere")
    private Integer idBordereauMere;

    @Column(name = "date_bordereau", nullable = false)
    private LocalDateTime dateBordereau;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_demande_mere", nullable = false)
    private DemandeMere demandeMere;

    @ManyToOne(optional = true)
    @JoinColumn(name = "id_devise")
    private Devise devise;

    @ManyToOne(optional = true)
    @JoinColumn(name = "id_transport",nullable = true)
    private Transport transport;

    @Column(name = "description")
    private String description;


    @Column(name = "colisage")
    private Integer colisage;

    @Column(name = "poids")
    private Double poids;



    // Getters et Setters



    public Integer getIdBordereauMere() {
        return idBordereauMere;
    }

    public void setIdBordereauMere(Integer idBordereauMere) {
        this.idBordereauMere = idBordereauMere;
    }

    public LocalDateTime getDateBordereau() {
        return dateBordereau;
    }

    public void setDateBordereau(LocalDateTime dateBordereau) {
        this.dateBordereau = dateBordereau;
    }

    public DemandeMere getDemandeMere() {
        return demandeMere;
    }

    public void setDemandeMere(DemandeMere demandeMere) {
        this.demandeMere = demandeMere;
    }

    public Devise getDevise() {
        return devise;
    }

    public void setDevise(Devise devise) {
        this.devise = devise;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getColisage() {
        return colisage;
    }

    public void setColisage(Integer colisage) {
        this.colisage = colisage;
    }

    public Double getPoids() {
        return poids;
    }

    public void setPoids(Double poids) {
        this.poids = poids;
    }

    public static Double getTotal(List<BordereauFille> bordereauFilleList)
    {
        Double result = 0.0;
        for (BordereauFille bordereauFille : bordereauFilleList)
        {
            result += bordereauFille.geTotal();
        }
        return result;
    }
}

