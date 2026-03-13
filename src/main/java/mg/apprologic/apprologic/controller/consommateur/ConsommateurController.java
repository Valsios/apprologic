package mg.apprologic.apprologic.controller.consommateur;

import mg.apprologic.apprologic.model.consommateur.Consommateur;
import mg.apprologic.apprologic.services.bons.BordereauFilleService;
import mg.apprologic.apprologic.services.consommateur.ConsommateurService;
import mg.apprologic.apprologic.util.MapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/consommateur")
public class ConsommateurController {

    @Autowired
    ConsommateurService consommateurService;


    @Autowired
    BordereauFilleService bordereauFilleService;

    protected List<String> getMonths()
    {
        return new ArrayList<>(Arrays.asList("Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Aout", "Septembre", "Octobre", "Novembre", "Décembre"));
    }
    @PostMapping("/consommations")
    public String consommationsConsommateur(Model model ,@RequestParam Integer idConsommateur ,@RequestParam(required = false) Integer year,@RequestParam(required = false) String month)
    {
        Integer currentYear = (year != null) ? year : LocalDateTime.now().getYear();
        Integer currentMonth = (month != null && !month.isEmpty()) ? Integer.parseInt(month) : null;

        Consommateur consommateur =  consommateurService.getById(idConsommateur);
        //available years
        List<Integer> availableYears = new ArrayList<>();
        int current = LocalDateTime.now().getYear();
        for (int i = current - 5; i <= current; i++) {
            availableYears.add(i);
        }


        HashMap<String,Double> consommations = MapUtil.transformKey(bordereauFilleService.consommationDepartementParArticle(currentYear, consommateur,currentMonth));

        model.addAttribute("months",getMonths());
        model.addAttribute("currentYear",currentYear);
        model.addAttribute("currentMonth",currentMonth);
        model.addAttribute("consommateur",consommateur);
        model.addAttribute("availableYears", availableYears);
        model.addAttribute("consommations",consommations );

        return "consommateur/Consommations";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Consommateur consommateur,@RequestParam(required = false) String consommateurMereId)
    {
        if (consommateurMereId != null && !consommateurMereId.isEmpty())
        {
            consommateur.setMere(consommateurService.getById(Integer.parseInt(consommateurMereId)));
        }
        consommateurService.save(consommateur);
        return "redirect:/consommateur/organigramme";
    }

    @PostMapping("/edit")
    public String editConsommateur(Model model ,@RequestParam Integer idConsommateur)
    {
        List<Consommateur> consommateurList = consommateurService.getAllMere();
        for (Consommateur consommateur : consommateurList)
        {
            consommateur.setEnfants(consommateurService.getAllEnfant(consommateur));
        }

        model.addAttribute("consommateurList",consommateurList);
        model.addAttribute("new_consommateur",consommateurService.getById(idConsommateur));
        return "consommateur/Organigramme";
    }

    @GetMapping("/organigramme")
    public String organigramme(Model model)
    {
        List<Consommateur> consommateurList = consommateurService.getAllMere();
        for (Consommateur consommateur : consommateurList)
        {
            consommateur.setEnfants(consommateurService.getAllEnfant(consommateur));
        }

        model.addAttribute("consommateurList",consommateurList);
        model.addAttribute("new_consommateur",new Consommateur());
        return "consommateur/Organigramme";

    }
    @GetMapping("/enfants/{idMere}")
    @ResponseBody
    public List<Consommateur> getEnfant(@PathVariable Integer idMere)
    {
        return consommateurService.getAllEnfant(consommateurService.getById(idMere));
    }
}
