package mg.apprologic.apprologic.controller.bons;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import mg.apprologic.apprologic.model.bons.*;

import mg.apprologic.apprologic.model.stock.StockFille;
import mg.apprologic.apprologic.model.stock.StockMere;
import mg.apprologic.apprologic.services.article.ArticleService;
import mg.apprologic.apprologic.services.article.DeviseService;
import mg.apprologic.apprologic.services.bons.BonLivraisonFilleService;
import mg.apprologic.apprologic.services.bons.BonLivraisonMereService;
import mg.apprologic.apprologic.services.fournisseur.FournisseurService;
import mg.apprologic.apprologic.services.local.GisementStockFilleService;
import mg.apprologic.apprologic.services.stock.StockFilleService;
import mg.apprologic.apprologic.services.stock.StockMereService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.io.IOException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/bonLivraison")
public class BonLivraisonController {

    @Autowired
    FournisseurService fournisseurService;

    @Autowired
    DeviseService deviseService;

    @Autowired
    BonLivraisonMereService bonLivraisonMereService;

    @Autowired
    BonLivraisonFilleService bonLivraisonFilleService;

    @Autowired
    ArticleService articleService;

    @Autowired
    StockMereService stockMereService;

    @Autowired
    StockFilleService stockFilleService;

    @Autowired
    GisementStockFilleService gisementStockFilleService;


    @PostMapping("/filtrer")
    public String filtrer(Model model,@RequestParam(required = false) String idFournisseur,@RequestParam(required = false) String debut,@RequestParam(required = false) String fin)
    {

        if (idFournisseur != null && !idFournisseur.isEmpty())
        {
            model.addAttribute("fournisseurId",Integer.parseInt(idFournisseur));
        }

        model.addAttribute("debut",LocalDateTime.parse(debut));
        model.addAttribute("fin",LocalDateTime.parse(fin));
        model.addAttribute("fournisseur_liste",fournisseurService.getAll());
        model.addAttribute("livraison_liste",bonLivraisonMereService.getByFournisseurDate(idFournisseur,debut,fin));
        return "bons/BonLivraisonListe";
    }
    @GetMapping("/liste")
    public String getListeBl(Model model)
    {
        LocalDateTime debut = LocalDateTime.now().minusDays(2);
        LocalDateTime fin = LocalDateTime.now();
        model.addAttribute("debut",debut);
        model.addAttribute("fin",fin);
        model.addAttribute("fournisseur_liste",fournisseurService.getAll());
        model.addAttribute("livraison_liste",bonLivraisonMereService.getByFournisseurDate(null,debut.toString(),fin.toString()));
        return "bons/BonLivraisonListe";
    }
    @GetMapping("/formulaire")
    public String getFormBl(Model model)
    {
        BonLivraisonMere bonLivraisonMere = new BonLivraisonMere();
        bonLivraisonMere.setDateReception(LocalDateTime.now());
        model.addAttribute("bonLivraison", bonLivraisonMere);
        model.addAttribute("fournisseur_liste",fournisseurService.getAll());
        model.addAttribute("devise_liste",deviseService.getAll());
        return "bons/BonLivraisonFormulaire";
    }

    @GetMapping("/viewFile/{livraisonId}")
    public ResponseEntity<byte[]> viewFile(@PathVariable Integer livraisonId) {
        byte[] fileContent = bonLivraisonMereService.getPieceJointeById(livraisonId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .contentType(MediaType.APPLICATION_PDF)
                .body(fileContent);
    }

    @PostMapping("/uploadFile")
    @Transactional
    public ResponseEntity<Map<String, String>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "livraisonId", required = false) Integer livraisonId) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Erreur lors de l'upload: fichier vide"
            ));
        }
        try {
            BonLivraisonMere bonLivraisonMere = bonLivraisonMereService.getById(livraisonId);
            bonLivraisonMere.setPieceJointe(file.getBytes());
            bonLivraisonMereService.save(bonLivraisonMere);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Fichier uploadé avec succès: " + file.getOriginalFilename()
            ));

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Erreur lors de l'upload: " + e.getMessage()
            ));
        }
    }


    @PostMapping("/save")
    @Transactional
    public String saveBl(
            @ModelAttribute("bonLivrason") BonLivraisonMere bonLivraisonMere
                        , HttpServletRequest request, RedirectAttributes redirectAttributes, HttpServletResponse response)
    {

        StringBuilder repartitionString = new StringBuilder();
        StringBuilder builderErrorRepartition = new StringBuilder();
        List<BonLivraisonFille> bonLivraisonFilleList = new ArrayList<>();
        try {

            //process BL
            bonLivraisonMereService.save(bonLivraisonMere);
            processLignesLivraison(request,bonLivraisonMere,bonLivraisonFilleList);

            //process gisement
            processGisement(bonLivraisonFilleList,repartitionString,builderErrorRepartition);

            //process stock
            StockMere stockMere = new StockMere();
            stockMere.setDescription("Livraison lié au numero :"+bonLivraisonMere.getIdBlMere());
            stockMere.setBonLivraisonMere(bonLivraisonMere);
            stockMere.setDateMouvement(bonLivraisonMere.getDateReception());

            stockMereService.save(stockMere);
            processStock(stockMere,bonLivraisonFilleList);

            // Ajouter la répartition aux flash attributes
            String fileName = "repartitionBL_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv";
            String csvContent = "Article;Gisement;quantité;udm\n" + repartitionString.toString().replace(" ","_");

            redirectAttributes.addFlashAttribute("csvContent", csvContent);
            redirectAttributes.addFlashAttribute("csvFileName", fileName);
            redirectAttributes.addFlashAttribute("showDownload", true);
            redirectAttributes.addFlashAttribute("success", "Livraison réussie.");
            if (!builderErrorRepartition.isEmpty())
            {
                redirectAttributes.addFlashAttribute("warning", builderErrorRepartition.toString());
            }

        }
        catch (Exception e)
        {
                for (BonLivraisonFille bonLivraisonFille : bonLivraisonFilleList)
                {
                    bonLivraisonFilleService.delete(bonLivraisonFille);
                }
                bonLivraisonMereService.delete(bonLivraisonMere);

            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error",e.getMessage());
            return "redirect:/bonLivraison/formulaire";
        }
        return "redirect:/bonLivraison/liste";

    }
    public void processLignesLivraison(HttpServletRequest request, BonLivraisonMere bonLivraisonMere, List<BonLivraisonFille> livraisonFilleList) throws Exception {
        int i = 0;
        while (request.getParameter("lignes"+i+".idArticle") != null)
        {
            String suffixe = "lignes"+i+".";
            Integer idArticle = Integer.valueOf(request.getParameter(suffixe+"idArticle"));

            BonLivraisonFille bonLivraisonFille = new BonLivraisonFille();
            bonLivraisonFille.setBonLivraisonMere(bonLivraisonMereService.getById(bonLivraisonMere.getIdBlMere()));
            bonLivraisonFille.setArticle(articleService.getById(idArticle));
            bonLivraisonFille.setPrixUnitaire(request.getParameter(suffixe+"PU"));
            bonLivraisonFille.setQuantite_demande(request.getParameter(suffixe+"quantiteDemande"));
            bonLivraisonFille.setQuantite_recu(request.getParameter(suffixe+"quantiteReçu"));

            bonLivraisonFilleService.save(bonLivraisonFille);



            livraisonFilleList.add(bonLivraisonFille);

            i++;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processGisement(List<BonLivraisonFille> bonLivraisonFilleList,StringBuilder repartitionString,StringBuilder errorRepartition)
    {
        for (BonLivraisonFille bonLivraisonFille : bonLivraisonFilleList)
        {
            gisementStockFilleService.firstInBonLivraison(bonLivraisonFille,repartitionString,errorRepartition);
        }
    }


    public void processStock(StockMere stockMere,List<BonLivraisonFille> bonLivraisonFilleListe)
    {
        for (BonLivraisonFille bonLivraisonFille : bonLivraisonFilleListe)
        {
            StockFille stockFille = new StockFille();
            stockFille.setEntree(bonLivraisonFille.getQuantite_recu());
            stockFille.setStockMere(stockMere);
            stockFille.setUdm(bonLivraisonFille.getArticle().getUdm());
            stockFille.setSortie(0.0);
            stockFille.setArticle(bonLivraisonFille.getArticle());

            stockFilleService.save(stockFille);
        }
    }
}
