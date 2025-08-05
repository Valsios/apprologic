package mg.apprologic.apprologic.controller.consommateur;

import mg.apprologic.apprologic.model.consommateur.Consommateur;
import mg.apprologic.apprologic.services.consommateur.ConsommateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/consommateur")
public class ConsommateurController {

    @Autowired
    ConsommateurService consommateurService;
    @GetMapping("/enfants/{idMere}")
    @ResponseBody
    public List<Consommateur> getEnfant(@PathVariable Integer idMere)
    {
        return consommateurService.getAllEnfant(consommateurService.getById(idMere));
    }
}
