package mg.apprologic.apprologic.model.article;
import jakarta.persistence.*;

@Entity
@Table(name = "devise")
public class Devise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_devise")
    private Integer idDevise;

    @Column(name = "designation", nullable = false)
    private String designation;

    @Column(name = "acronyme", nullable = false)
    private String acronyme;

    // Getters et Setters

    public Integer getIdDevise() {
        return idDevise;
    }

    public void setIdDevise(Integer idDevise) {
        this.idDevise = idDevise;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getAcronyme() {
        return acronyme;
    }

    public void setAcronyme(String acronyme) {
        this.acronyme = acronyme;
    }
}

