package mg.apprologic.apprologic.model.local;
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

    @Column(name = "colonne", nullable = false)
    private String colonne;

    @Column(name = "ligne", nullable = false)
    private Integer ligne;

    @Column(name = "numero", nullable = false)
    private Integer numero;

    @Column(name = "ligne_fille", nullable = false)
    private Integer ligneFille;

    // Getters et Setters

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

    public String getColonne() {
        return colonne;
    }

    public void setColonne(String colonne) {
        this.colonne = colonne;
    }

    public Integer getLigne() {
        return ligne;
    }

    public void setLigne(Integer ligne) {
        this.ligne = ligne;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getLigneFille() {
        return ligneFille;
    }

    public void setLigneFille(Integer ligneFille) {
        this.ligneFille = ligneFille;
    }
}

