package mg.apprologic.apprologic.model.consommateur;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "adresse")
public class Adresse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_adresse")
    private Integer idAdresse;

    @Column(name = "adresse", length = 255)
    private String adresse;

    // Constructors
    public Adresse() {
    }

    public Adresse(String adresse) {
        this.adresse = adresse;
    }

    // Getters and Setters

    @JsonProperty("idAdresse")
    public Integer getIdAdresse() {
        return idAdresse;
    }

    public void setIdAdresse(Integer idAdresse) {
        this.idAdresse = idAdresse;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

}
