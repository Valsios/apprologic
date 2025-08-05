package mg.apprologic.apprologic.services.utilisateur;

import mg.apprologic.apprologic.exception.ExceptionLogin;
import mg.apprologic.apprologic.model.utilisateur.Utilisateur;
import mg.apprologic.apprologic.repository.utilisateur.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurService implements UserDetailsService {
    @Autowired
    UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.getUtilisateurByLogin(login);
        return utilisateur;
    }

    public void save(Utilisateur utilisateur)
    {
        utilisateurRepository.save(utilisateur);
    }

    public void desactivate(Utilisateur utilisateur)
    {
        utilisateur.setStatus(0);
        utilisateurRepository.save(utilisateur);
    }
}
