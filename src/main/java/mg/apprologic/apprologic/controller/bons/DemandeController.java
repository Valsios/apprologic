package mg.apprologic.apprologic.controller.bons;


import jakarta.servlet.http.HttpServletRequest;
import mg.apprologic.apprologic.model.article.Article;
import mg.apprologic.apprologic.model.bons.DemandeFille;
import mg.apprologic.apprologic.model.bons.DemandeMere;
import mg.apprologic.apprologic.model.consommateur.Adresse;
import mg.apprologic.apprologic.model.consommateur.Consommateur;
import mg.apprologic.apprologic.services.article.ArticleService;
import mg.apprologic.apprologic.services.bons.BonCommandeFilleService;
import mg.apprologic.apprologic.services.bons.BonCommandeMereService;
import mg.apprologic.apprologic.services.consommateur.AdresseService;
import mg.apprologic.apprologic.services.consommateur.ConsommateurService;
import mg.apprologic.apprologic.services.stock.StockFilleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/demande")
public class DemandeController {

    @Autowired
    ConsommateurService consommateurService;

    @Autowired
    AdresseService adresseService;

    @Autowired
    ArticleService articleService;

    @Autowired
    BonCommandeMereService bonCommandeMereService;

    @Autowired
    BonCommandeFilleService bonCommandeFilleService;

    @Autowired
    StockFilleService stockFilleService;


    @GetMapping("/liste")
    public String getListeBonCommande(Model model)
    {
        model.addAttribute("commande_liste",bonCommandeMereService.getAllNotSortie());
        return "bons/DemandeListe";
    }
    @GetMapping("/formulaire")
    public String getFormulaireBonCommande(Model model)
    {
        model.addAttribute("consommateur_liste",consommateurService.getAllMere());
        model.addAttribute("adresse_liste",adresseService.getAll());
        return "bons/DemandeFormulaire";
    }



    @PostMapping("/save")
    public String enregistrerCommande(
            @RequestParam("consommateur") Integer idConsommateur,
            @RequestParam("adresse") Integer idAdresse,
            @RequestParam("codePgi") String codePgi,
            @RequestParam("consommateurEnfant") Integer idConsommateurEnfant,
            @RequestParam("date-commande")String dateCommande,
            HttpServletRequest request, RedirectAttributes redirectAttributes) {


        DemandeMere commandeMere =null;
        List<DemandeFille> demandeFilleList = new ArrayList<DemandeFille>();
        try
        {// 1. Validation des données
            Integer idConsommateurFinal = idConsommateur;
            if (idConsommateurEnfant != null)
            {
                idConsommateurFinal = idConsommateurEnfant;
            }
            Consommateur consommateur = consommateurService.getById(idConsommateurFinal);
            Adresse adresse = adresseService.getById(idAdresse);

            if (checkIfAnyCommande(request)==true)
            {
                throw new Exception("Pas d'articles trouvés.");
            }
            // 2. Création du bon de commande mère
            commandeMere = new DemandeMere();
            commandeMere.setDateDemande(LocalDateTime.parse(dateCommande));
            commandeMere.setConsommateur(consommateur);
            commandeMere.setAdresse(adresse);
            commandeMere.setCodePgi(codePgi);

            DemandeMere savedMere = bonCommandeMereService.save(commandeMere);


            // 3. Traitement des lignes de commande
            processLignesCommande(request, savedMere, demandeFilleList,redirectAttributes);

            redirectAttributes.addFlashAttribute("success", "Demande enregistré avec succès");
        }
        catch (Exception e)
        {
            for (DemandeFille demandeFille : demandeFilleList)
            {
                bonCommandeFilleService.delete(demandeFille);
            }
            if (commandeMere != null)
            {
                bonCommandeMereService.delete(commandeMere);
            }
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/demande/formulaire";
        }
        return "redirect:/demande/liste";
    }
    private boolean checkIfAnyCommande(HttpServletRequest request)
    {
        int i = 0;
        while (request.getParameter("lignes"+i+".idArticle") != null)
        {
            i++;
        }
        return i==0;
    }
    private void processLignesCommande(HttpServletRequest request, DemandeMere commandeMere, List<DemandeFille> commandeFilleList,RedirectAttributes redirectAttributes) throws Exception {
        int i = 0;
        String warning = "";
        while (request.getParameter("lignes"+i+".idArticle") != null)
        {
            String suffixe = "lignes"+i+".";
            Integer idArticle = Integer.valueOf(request.getParameter(suffixe+"idArticle"));

            DemandeFille demandeFille = new DemandeFille();
            Article article = articleService.getById(idArticle);
            Double stock_article = stockFilleService.stock_article_date(commandeMere.getDateDemande(),article);
            demandeFille.setArticle(article);
            demandeFille.setDemandeMere(commandeMere);
            demandeFille.setQuantite(request.getParameter(suffixe+"quantite"));
            demandeFille.setUdm(article.getUdm());
            if ((stock_article-demandeFille.getQuantite())<0)
            {
                warning += "\n-"+article.getDesignation()+" en épuisement. Stock restant "+stock_article+" pour "+demandeFille.getQuantite()+" demandé.";
            }


            bonCommandeFilleService.save(demandeFille);

            commandeFilleList.add(demandeFille);

            i++;
        }
        if (!warning.isEmpty())
        {
            redirectAttributes.addFlashAttribute("warning","WARNING : \n"+warning);
        }
    }
}
