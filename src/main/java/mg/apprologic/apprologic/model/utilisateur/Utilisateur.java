package mg.apprologic.apprologic.model.utilisateur;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "utilisateur")
public class Utilisateur implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_utilisateur")
    private Integer idUtilisateur;

    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Column(name = "mdp", nullable = false)
    private String mdp;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "prenom", nullable = false)
    private String prenom;

    @Column(name = "contact", nullable = false)
    private String contact;

    @Column(name = "status")
    private Integer status;

    // Getters et Setters

    public Integer getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(Integer idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }



    //details of log
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Vous n'avez qu'un rôle : ADMIN
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Override
    public String getPassword() {
        return this.mdp; // Assurez-vous que le mot de passe est déjà hashé en BCrypt !
    }

    @Override
    public String getUsername() {
        return this.login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Compte jamais expiré
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Compte jamais bloqué
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Mot de passe toujours valide
    }

    @Override
    public boolean isEnabled() {
        return this.status == 1; // Si status=1 → compte actif
    }


}

