package mg.apprologic.apprologic.model.article;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "centre_budgetaire")
public class CentreBudgetaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_centre")
    private Integer idCentre;


    @Column(name = "code_centre")
    private String codeCentre;


    @JsonProperty("idCentre")
    public Integer getIdCentre() {
        return idCentre;
    }

    public void setIdCentre(Integer idCentre) {
        this.idCentre = idCentre;
    }


    @JsonProperty("codeCentre")
    public String getCodeCentre() {
        return codeCentre;
    }

    public void setCodeCentre(String codeCentre) {
        this.codeCentre = codeCentre;
    }
}
