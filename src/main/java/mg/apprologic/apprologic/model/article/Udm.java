package mg.apprologic.apprologic.model.article;
import jakarta.persistence.*;

@Entity
@Table(name = "udm")
public class Udm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_udm")
    private Integer idUdm;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "acronyme", nullable = false)
    private String acronyme;

    // Getters et Setters

    public Integer getIdUdm() {
        return idUdm;
    }

    public void setIdUdm(Integer idUdm) {
        this.idUdm = idUdm;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAcronyme() {
        return acronyme;
    }

    public void setAcronyme(String acronyme) {
        this.acronyme = acronyme;
    }
}

