package mg.apprologic.apprologic.model.fournisseur;
import jakarta.persistence.*;

@Entity
@Table(name = "fournisseur")
public class Fournisseur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_fournisseur")
    private Integer idFournisseur;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "contact", nullable = false)
    private String contact;

    @Column(name = "mail", nullable = false)
    private String mail;

    // Getters et Setters

    public Integer getIdFournisseur() {
        return idFournisseur;
    }

    public void setIdFournisseur(Integer idFournisseur) {
        this.idFournisseur = idFournisseur;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}

