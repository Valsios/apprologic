package mg.apprologic.apprologic.model.bons;

import jakarta.persistence.*;
import mg.apprologic.apprologic.model.article.Devise;
import mg.apprologic.apprologic.model.fournisseur.Fournisseur;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;

import java.sql.Types;
import java.time.LocalDateTime;
import java.sql.Date;

@Entity
@Table(name = "bon_livraison_mere")
public class BonLivraisonMere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bl_mere")
    private Integer idBlMere;



    @ManyToOne(optional = false)
    @JoinColumn(name = "id_fournisseur", nullable = false)
    private Fournisseur fournisseur;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_devise", nullable = false)
    private Devise devise;

    @Column(name = "date_reception", nullable = false)
    private LocalDateTime dateReception;

    @Column(name = "description")
    private String description;


    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name = "piece_jointe")
    private byte[] pieceJointe;

    // Getters et Setters
    public byte[] getPieceJointe() {
        return pieceJointe;
    }

    public void setPieceJointe(byte[] pieceJointe) {
        this.pieceJointe = pieceJointe;
    }

    // Getters et Setters

    public Integer getIdBlMere() {
        return idBlMere;
    }

    public void setIdBlMere(Integer idBlMere) {
        this.idBlMere = idBlMere;
    }

    public Devise getDevise() {
        return devise;
    }

    public void setDevise(Devise devise) {
        this.devise = devise;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }


    public LocalDateTime getDateReception() {
        return dateReception;
    }

    public void setDateReception(LocalDateTime dateReception) {
        this.dateReception = dateReception;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

