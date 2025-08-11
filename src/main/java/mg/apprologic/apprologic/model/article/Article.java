package mg.apprologic.apprologic.model.article;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "article")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_article")
    private Integer idArticle;

    @Column(name = "designation", nullable = false)
    private String designation;

    @Column(name = "code_article", nullable = false)
    private String codeArticle;

    @Column(name = "seuil_min", nullable = false)
    private Double seuilMin;

    // Getters et Setters
    @ManyToOne(optional = false,fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "id_udm", nullable = false)
    private Udm udm;

    @Transient
    static Integer defaultDelayDemand = 5;

    public static Integer getDefaultDelayDemand() {
        return defaultDelayDemand;
    }

    public static void setDefaultDelayDemand(Integer defaultDelayDemand) {
        Article.defaultDelayDemand = defaultDelayDemand;
    }

    @JsonProperty("idArticle")
    public Integer getIdArticle() {
        return idArticle;
    }

    public void setIdArticle(Integer idArticle) {
        this.idArticle = idArticle;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    @JsonProperty("codeArticle")
    public String getCodeArticle() {
        return codeArticle;
    }

    public void setCodeArticle(String codeArticle) {
        this.codeArticle = codeArticle;
    }


    @JsonProperty("udm")
    public Udm getUdm() {
        return udm;
    }

    public void setUdm(Udm udm) {
        this.udm = udm;
    }

    @JsonProperty("seuilMin")
    public Double getSeuilMin() {
        return seuilMin;
    }

    public void setSeuilMin(Double seuilMin) {
        this.seuilMin = seuilMin;
    }
}
