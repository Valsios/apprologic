package mg.apprologic.apprologic.model.bons;

import jakarta.persistence.*;
import mg.apprologic.apprologic.model.consommateur.Adresse;
import mg.apprologic.apprologic.model.consommateur.Consommateur;

import java.time.LocalDateTime;

@Entity
@Table(name = "demande_mere")
public class DemandeMere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_demande_mere")
    private Integer idDemandeMere;

    @Column(name = "date_demande", nullable = false)
    private LocalDateTime dateDemande;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_consommateur", nullable = false)
    private Consommateur consommateur;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_adresse", nullable = false)
    private Adresse adresse;

    @Column(name = "date_sortie", nullable = true)
    private LocalDateTime dateSortie;

    @Column(name = "code_pgi", nullable = false)
    private String codePgi;

    // Getters et Setters


    public Integer getIdDemandeMere() {
        return idDemandeMere;
    }

    public void setIdDemandeMere(Integer idDemandeMere) {
        this.idDemandeMere = idDemandeMere;
    }

    public LocalDateTime getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(LocalDateTime dateDemande) {
        this.dateDemande = dateDemande;
    }

    public Consommateur getConsommateur() {
        return consommateur;
    }

    public void setConsommateur(Consommateur consommateur) {
        this.consommateur = consommateur;
    }

    public LocalDateTime getDateSortie() {
        return dateSortie;
    }

    public void setDateSortie(LocalDateTime dateSortie) {
        this.dateSortie = dateSortie;
    }

    public String getCodePgi() {
        return codePgi;
    }

    public void setCodePgi(String codePgi) {
        this.codePgi = codePgi;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }
}

