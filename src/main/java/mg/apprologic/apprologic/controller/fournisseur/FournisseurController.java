package mg.apprologic.apprologic.controller.fournisseur;

import mg.apprologic.apprologic.model.fournisseur.Fournisseur;
import mg.apprologic.apprologic.services.fournisseur.FournisseurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/fournisseur")
public class FournisseurController {

    @Autowired
    FournisseurService fournisseurService;
    @GetMapping("/ajout")
    public String afficherFormulaireAjout(Model model) {
        model.addAttribute("fournisseur", new Fournisseur());
        return "fournisseur/AjoutFournisseur";
    }

    @PostMapping("/save")
    public String saveOrUpdateFournisseur(@ModelAttribute Fournisseur fournisseur) {
        fournisseurService.save(fournisseur);
        return "redirect:/fournisseur/liste";
    }

    @GetMapping("/liste")
    public String afficherListeFournisseurs(Model model) {
        List<Fournisseur> liste = fournisseurService.getAll();
        model.addAttribute("fournisseurListe",liste);
        return "fournisseur/ListeFournisseur";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        Fournisseur fournisseur = fournisseurService.getById(id);
        model.addAttribute("fournisseur",fournisseur);
        return "fournisseur/EditFournisseur";
    }
}

