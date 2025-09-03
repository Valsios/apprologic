package mg.apprologic.apprologic.controller.article;


import mg.apprologic.apprologic.model.bons.BonLivraisonMere;
import mg.apprologic.apprologic.model.bons.BordereauMere;
import mg.apprologic.apprologic.services.bons.BonLivraisonMereService;
import mg.apprologic.apprologic.services.bons.BordereauMereService;
import mg.apprologic.apprologic.util.MapUtil;
import org.bouncycastle.math.raw.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/analyse")
public class DahsboardController {


    @Autowired
    BonLivraisonMereService bonLivraisonMereService;

    @Autowired
    BordereauMereService bordereauMereService;

    @GetMapping("/dashboard")
    public String globalGet( Model model)
    {
        Integer currentYear = LocalDateTime.now().getYear();

        //chiffre des demandes et bordereaux
        List<BordereauMere> bordereauMereList = bordereauMereService.getByYear(currentYear);
        HashMap<String,Double> chiffreAboutDemande = bordereauMereService.totalDemande(currentYear,bordereauMereList);
        HashMap<String,Double> consommationParDepartement = MapUtil.transformKey(bordereauMereService.consommationValeurParDepartement(bordereauMereList));

        //chiffre des livraisons
        List<BonLivraisonMere> bonLivraisonMereList = bonLivraisonMereService.getByYear(currentYear);
        HashMap<String,Double> satisfactionParFournisseur =MapUtil.transformKey(bonLivraisonMereService.satisfactionFournisseurGlobal(bonLivraisonMereList));
        Integer countLivraison = bonLivraisonMereService.countLivraison(bonLivraisonMereList);


        //available years
        List<Integer> availableYears = new ArrayList<>();
        int current = LocalDateTime.now().getYear();
        for (int i = current - 5; i <= current; i++) {
            availableYears.add(i);
        }

        model.addAttribute("currentYear", currentYear);
        model.addAttribute("availableYears", availableYears);
        model.addAttribute("chiffreAboutDemande",chiffreAboutDemande);
        model.addAttribute("consommationParDepartement",consommationParDepartement);
        model.addAttribute("satisfactionParFournisseur",satisfactionParFournisseur);
        model.addAttribute("countLivraison",countLivraison);
        model.addAttribute("satisfactionTotal",bonLivraisonMereService.satisfactionParLivraison(bonLivraisonMereList));
        model.addAttribute("evolutionDepense",bordereauMereService.evolutionDepense(bordereauMereList));

        return "/analyse/DashboardGlobal";

    }
    @PostMapping("/filtrer")
    public String global(@RequestParam(required = false) Integer year, Model model)
    {
        Integer currentYear = (year != null) ? year : LocalDateTime.now().getYear();

        //chiffre des demandes et bordereaux
        List<BordereauMere> bordereauMereList = bordereauMereService.getByYear(currentYear);
        HashMap<String,Double> chiffreAboutDemande = bordereauMereService.totalDemande(year,bordereauMereList);
        HashMap<String,Double> consommationParDepartement = MapUtil.transformKey(bordereauMereService.consommationValeurParDepartement(bordereauMereList));

        //chiffre des livraisons
        List<BonLivraisonMere> bonLivraisonMereList = bonLivraisonMereService.getByYear(currentYear);
        HashMap<String,Double> satisfactionParFournisseur =MapUtil.transformKey(bonLivraisonMereService.satisfactionFournisseurGlobal(bonLivraisonMereList));
        Integer countLivraison = bonLivraisonMereService.countLivraison(bonLivraisonMereList);


        //available years
        List<Integer> availableYears = new ArrayList<>();
        int current = LocalDateTime.now().getYear();
        for (int i = current - 5; i <= current; i++) {
            availableYears.add(i);
        }

        model.addAttribute("currentYear", currentYear);
        model.addAttribute("availableYears", availableYears);
        model.addAttribute("chiffreAboutDemande",chiffreAboutDemande);
        model.addAttribute("consommationParDepartement",consommationParDepartement);
        model.addAttribute("satisfactionParFournisseur",satisfactionParFournisseur);
        model.addAttribute("countLivraison",countLivraison);
        model.addAttribute("satisfactionTotal",bonLivraisonMereService.satisfactionParLivraison(bonLivraisonMereList));
        model.addAttribute("evolutionDepense",bordereauMereService.evolutionDepense(bordereauMereList));

        return "/analyse/DashboardGlobal";

    }
}
