package mg.apprologic.apprologic.model.local;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "existant_gisement")
public class ExistantGisement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_gisement")
    private Integer idGisement;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_local", nullable = false)
    private Local local;

    @Column(name = "trave", nullable = false)
    private Integer trave;

    @Column(name = "alveole", nullable = false)
    private String alveole;

    @Column(name = "etagere", nullable = false)
    private Integer etagere;

    @Column(name = "bac", nullable = false)
    private Integer bac;

    // Getters et Setters

    @JsonProperty("idGisement")
    public Integer getIdGisement() {
        return idGisement;
    }

    public void setIdGisement(Integer idGisement) {
        this.idGisement = idGisement;
    }

    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

    public Integer getTrave() {
        return trave;
    }

    public void setTrave(Integer trave) {
        this.trave = trave;
    }

    public String getAlveole() {
        return alveole;
    }

    public void setAlveole(String alveole) {
        this.alveole = alveole;
    }

    public Integer getEtagere() {
        return etagere;
    }

    public void setEtagere(Integer etagere) {
        this.etagere = etagere;
    }

    public Integer getBac() {
        return bac;
    }

    public void setBac(Integer bac) {
        this.bac = bac;
    }

    public String toString()
    {
        return this.getLocal().getDesignation()+" "+this.getTrave()+" "+this.getAlveole()+" " +this.getEtagere()+" "+this.getBac()+";";
    }
}

