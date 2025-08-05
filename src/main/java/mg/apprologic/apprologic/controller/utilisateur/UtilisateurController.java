package mg.apprologic.apprologic.controller.utilisateur;

import jakarta.servlet.http.HttpServletRequest;
import mg.apprologic.apprologic.model.utilisateur.Utilisateur;
import mg.apprologic.apprologic.services.utilisateur.UtilisateurService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.apache.commons.codec.digest.DigestUtils.md5;

@Controller
@RequestMapping("/user")
public class UtilisateurController {

    @Autowired
    UtilisateurService utilisateurService;

    @Autowired
    PasswordEncoder passwordEncoder;
    @GetMapping("/ajout")
    public String getFormajout(HttpServletRequest request, Model model)
    {
        model.addAttribute("utilisateur",new Utilisateur());
        return "utilisateur/AjoutUtilisateur";
    }

    @PostMapping("/save")
    public String save(Utilisateur utilisateur,HttpServletRequest request)
    {

        utilisateur.setStatus(1);
        utilisateur.setMdp(passwordEncoder.encode(utilisateur.getMdp()));
        utilisateurService.save(utilisateur);
        return "redirect:/";
    }

    @PostMapping("/desactivate")
    public String desactivate(

    ) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Utilisateur user = (Utilisateur) auth.getPrincipal();

        utilisateurService.desactivate(user);

        return "redirect:/user/loginPage?logout=true";
    }


}
