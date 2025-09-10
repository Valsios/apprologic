package mg.apprologic.apprologic.controller.bons;

import jakarta.servlet.http.HttpServletRequest;
import mg.apprologic.apprologic.exception.ExceptionDate;
import mg.apprologic.apprologic.exception.ExceptionValueNumber;
import mg.apprologic.apprologic.model.article.Article;
import mg.apprologic.apprologic.model.bons.*;
import mg.apprologic.apprologic.model.stock.StockFille;
import mg.apprologic.apprologic.model.stock.StockMere;
import mg.apprologic.apprologic.services.article.ArticleService;
import mg.apprologic.apprologic.services.article.DeviseService;
import mg.apprologic.apprologic.services.bons.DemandeFilleService;
import mg.apprologic.apprologic.services.bons.DemandeMereService;
import mg.apprologic.apprologic.services.bons.BordereauFilleService;
import mg.apprologic.apprologic.services.bons.BordereauMereService;
import mg.apprologic.apprologic.services.consommateur.ConsommateurService;
import mg.apprologic.apprologic.services.consommateur.TransportService;
import mg.apprologic.apprologic.services.local.GisementStockFilleService;
import mg.apprologic.apprologic.services.stock.StockFilleService;
import mg.apprologic.apprologic.services.stock.StockMereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/bordereau")
public class BordereauController {

    @Autowired
    DemandeMereService demandeMereService;

    @Autowired
    DemandeFilleService demandeFilleService;

    @Autowired
    DeviseService deviseService;

    @Autowired
    TransportService transportService;
    @Autowired
    ArticleService articleService;


    @Autowired
    BordereauMereService bordereauMereService;

    @Autowired
    BordereauFilleService bordereauFilleService;

    @Autowired
    StockFilleService stockFilleService;

    @Autowired
    StockMereService stockMereService;


    @Autowired
    GisementStockFilleService gisementStockFilleService;

    @Autowired
    ConsommateurService consommateurService;


    @PostMapping("/filtrer")
    public String filtrer(Model model,@RequestParam(required = false) String idConsommateur,@RequestParam(required = false) String idConsommateurFille,
                          @RequestParam(required = false) String debut,@RequestParam(required = false) String fin)
    {
        List<BordereauMere> toReturn  = new ArrayList<>();
        String final_id = "";
        if (!idConsommateur.isEmpty())
        {
           final_id = idConsommateur;
            if (idConsommateurFille != null && !idConsommateurFille.isEmpty())
            {
               final_id = idConsommateurFille;
            }
            model.addAttribute("consommateurId",Integer.parseInt(final_id));
        }
        model.addAttribute("debut",LocalDateTime.parse(debut));
        model.addAttribute("fin",LocalDateTime.parse(fin));
        toReturn = bordereauMereService.getByConsommateurDate(idConsommateur,debut,fin);

        model.addAttribute("consommateur_liste",consommateurService.getAllMere());
        model.addAttribute("bordereau_liste",toReturn);
        return "bons/BordereauListe";
    }

    @GetMapping("/liste")
    public String getListeBordereau(Model model)
    {
        LocalDateTime debut = LocalDateTime.now().minusMonths(1);
        LocalDateTime fin = LocalDateTime.now();
        model.addAttribute("debut",debut);
        model.addAttribute("fin",fin);
        model.addAttribute("consommateur_liste",consommateurService.getAllMere());
        model.addAttribute("bordereau_liste",bordereauMereService.getByConsommateurDate(null,debut.toString(),fin.toString()));
        return "bons/BordereauListe";
    }
    @PostMapping("/save")
    @Transactional
    public String save(@ModelAttribute("bordereau") BordereauMere bordereauMere, HttpServletRequest request, RedirectAttributes redirectAttributes)
    {
        StockMere stockMere = null;
        List<StockFille> stockFilleList = new ArrayList<StockFille>();
        List<BordereauFille> bordereauFilleList = new ArrayList<BordereauFille>();
        StringBuilder builderRepartition = new StringBuilder();
        try
        {

            bordereauMereService.save(bordereauMere);
            if (bordereauMere.getDateBordereau().isBefore(bordereauMere.getDemandeMere().getDateDemande()))
            {
                throw new ExceptionDate("Date bordereau invalide.");
            }

            saveBordereauFille(request,bordereauMere,bordereauFilleList,redirectAttributes);

            bordereauMere.getDemandeMere().setDateSortie(bordereauMere.getDateBordereau());
            demandeMereService.save(bordereauMere.getDemandeMere());



            //insertion stock
            stockMere = new StockMere();
            stockMere.setDemandeMere(bordereauMere.getDemandeMere());
            stockMere.setDescription("Sortie pour le bordereau :"+bordereauMere.getIdBordereauMere());
            stockMere.setDateMouvement(bordereauMere.getDateBordereau());

            stockMereService.save(stockMere);
            saveStockFille(bordereauFilleList,stockMere,stockFilleList);

            //process gisement
            processGisement(bordereauFilleList,builderRepartition);

            //data file repartition
            String fileName = "repartitionBD_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv";
            String csvContent = "Article;Gisement;Quantité;Vague\n" + builderRepartition.toString().replace(" ","_");

            redirectAttributes.addFlashAttribute("csvContent", csvContent);
            redirectAttributes.addFlashAttribute("csvFileName", fileName);
            redirectAttributes.addFlashAttribute("showDownload", true);
            //end of


            System.out.println("SAVED");
            redirectAttributes.addFlashAttribute("success", "Bordereau enregistré avec succès");


        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("NOT SAVED");
            for (StockFille stockFille : stockFilleList)
            {
                stockFilleService.delete(stockFille);
            }
            if (stockMere != null)
            {
                stockMereService.delete(stockMere);
            }
            for (BordereauFille bordereauFille:bordereauFilleList )
            {
                bordereauFilleService.delete(bordereauFille);
            }

            bordereauMereService.delete(bordereauMere);
            redirectAttributes.addFlashAttribute("error", "Erreur de création bordereau. "+e.getMessage());

            return "redirect:/demande/liste";
        }
        return "redirect:/bordereau/liste";
    }

    protected void saveStockFille(List<BordereauFille> bordereauFilleList,StockMere stockMere,List<StockFille> stockFilleList)
    {
        for (BordereauFille bordereauFille : bordereauFilleList)
        {
            StockFille stockFille = new StockFille();
            stockFille.setArticle(bordereauFille.getDemandeFille().getArticle());
            stockFille.setStockMere(stockMere);
            stockFille.setSortie(bordereauFille.getQuantiteSortie());
            stockFille.setEntree(0.0);
            stockFille.setUdm(stockFille.getArticle().getUdm());

            stockFilleList.add(stockFille);
            stockFilleService.save(stockFille);
        }
    }

    protected void saveBordereauFille(HttpServletRequest request , BordereauMere bordereauMere,List<BordereauFille> bordereauFilleList,RedirectAttributes redirectAttributes)throws Exception
    {
        String warning = "";
        List<DemandeFille> demandeFilleList = demandeFilleService.getByMere(bordereauMere.getDemandeMere());
        for (DemandeFille demandeFille : demandeFilleList)
        {
            String suffix = "lignes"+ demandeFille.getIdDemandeFille()+".";

            BordereauFille bordereauFille = new BordereauFille();
            bordereauFille.setDemandeFille(demandeFille);
            bordereauFille.setBordereauMere(bordereauMereService.getById(bordereauMere.getIdBordereauMere()));

            bordereauFille.setPrixUnitaire(Double.parseDouble(request.getParameter(suffix+"prixUnitaire"))/bordereauFille.getBordereauMere().getDevise().getCoursAriary());
            bordereauFille.setQuantiteSortie(request.getParameter(suffix+"quantiteLivree"));

            Article article = bordereauFille.getDemandeFille().getArticle();
            Double stock_article = stockFilleService.stock_article_date(bordereauMere.getDateBordereau(),article);


            if (stock_article<bordereauFille.getQuantiteSortie())
            {
                throw new ExceptionValueNumber("Stock article "+article.getDesignation()+" invalide avec "+stock_article+" en stock avec "+bordereauFille.getQuantiteSortie()+" sorties.");
            }
            if ((stock_article-bordereauFille.getQuantiteSortie())<article.getSeuilMin())
            {
                warning += "\n- Stock critique "+article.getDesignation()+" dépassant son seuil de "+article.getSeuilMin()+" "+article.getUdm().getAcronyme();
            }
            if (!warning.isEmpty())
            {
                redirectAttributes.addFlashAttribute("warning","WARNING : \n"+warning);
            }
            bordereauFilleService.save(bordereauFille);


            bordereauFilleList.add(bordereauFille);
        }
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processGisement(List<BordereauFille> bordereauFilleList,StringBuilder repartitionSortie)throws Exception
    {
        for (BordereauFille bordereauFille : bordereauFilleList)
        {
            gisementStockFilleService.firstOutBordereauFille(bordereauFille,repartitionSortie);
        }
    }
    @PostMapping("/create")
    public String getFormBordereau(
            @RequestParam("idBcMere") Integer idBcMere,
            Model model) {

        DemandeMere demandeMere = demandeMereService.getById(idBcMere);
        BordereauMere bordereauMere = new BordereauMere();
        bordereauMere.setDemandeMere(demandeMere);

        List<DemandeFille> demandeFilleList = demandeFilleService.getByMere(demandeMere);
        setPrixArticle(demandeFilleList);

        model.addAttribute("bordereau", bordereauMere);
        model.addAttribute("commandeFille_liste", demandeFilleList);

        model.addAttribute("devise_liste", deviseService.getAll());
        model.addAttribute("transport_liste", transportService.getAll());

        return "bons/BordereauFormulaire";
    }


    @Transactional
    protected void setPrixArticle(List<DemandeFille> demandeFilleList)
    {
        for(DemandeFille demandeFille : demandeFilleList){
            demandeFille.getArticle().setPrixPondere(articleService.prixPondereArticle(demandeFille.getArticle()));
        }
    }
}
