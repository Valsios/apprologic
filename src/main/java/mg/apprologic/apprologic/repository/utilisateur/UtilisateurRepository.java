package mg.apprologic.apprologic.repository.utilisateur;

import mg.apprologic.apprologic.model.utilisateur.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur,Integer> {
    @Query("select u from Utilisateur u where u.login = :login AND u.mdp = MD5(:mdp) ")
    Utilisateur getUtilisateurByLogin(@Param("login") String login ,@Param("mdp") String mdp);

    Utilisateur getUtilisateurByLogin(String login);
}
