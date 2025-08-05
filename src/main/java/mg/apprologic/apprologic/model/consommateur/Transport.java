package mg.apprologic.apprologic.model.consommateur;


import jakarta.persistence.*;


@Entity
@Table(name = "transport")
public class Transport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transport")
    private Integer idTransport;

    @Column(name = "type", length = 255, nullable = false)
    private String type;

    // Constructors
    public Transport() {
    }

    public Transport(String type) {
        this.type = type;
    }

    // Getters and Setters
    public Integer getIdTransport() {
        return idTransport;
    }

    public void setIdTransport(Integer idTransport) {
        this.idTransport = idTransport;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}