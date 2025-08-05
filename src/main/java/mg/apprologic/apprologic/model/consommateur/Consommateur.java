package mg.apprologic.apprologic.model.consommateur;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "consommateur")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Consommateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_consommateur")
    private Integer idConsommateur;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "rang", nullable = false)
    private String rang;

    @ManyToOne
    @JoinColumn(name = "id_mere", referencedColumnName = "id_consommateur", nullable = true)
    private Consommateur mere; // Nullable autorisé

    // Getters et Setters

    public Integer getIdConsommateur() {
        return idConsommateur;
    }

    public void setIdConsommateur(Integer idConsommateur) {
        this.idConsommateur = idConsommateur;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRang() {
        return rang;
    }

    public void setRang(String rang) {
        this.rang = rang;
    }

    public Consommateur getMere() {
        return mere;
    }

    public void setMere(Consommateur mere) {
        this.mere = mere;
    }
}
