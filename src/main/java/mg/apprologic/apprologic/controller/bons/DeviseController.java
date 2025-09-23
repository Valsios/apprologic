package mg.apprologic.apprologic.controller.bons;

import mg.apprologic.apprologic.model.article.Devise;
import mg.apprologic.apprologic.services.article.DeviseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/devise")
public class DeviseController {


    @Autowired
    DeviseService deviseService;
    @GetMapping("/gestion")
    public String getPageDevise(Model model)
    {
        model.addAttribute("devise",new Devise());
        model.addAttribute("devise_liste",deviseService.getAll());
        return "bons/DeviseGestion";
    }

    @PostMapping("/updateCours")
    public String updateCours(@RequestParam Integer idDevise ,@RequestParam Double nouveauCours)
    {
        Devise devise = deviseService.getById(idDevise);
        devise.setCoursAriary(nouveauCours);
        deviseService.save(devise);
        return "redirect:/devise/gestion";
    }

    @PostMapping("/save")
    public String updateCours(@ModelAttribute Devise devise)
    {
        deviseService.save(devise);
        return "redirect:/devise/gestion";
    }
}
