package mg.apprologic.apprologic.controller.bons;

import jakarta.servlet.http.HttpServletRequest;
import mg.apprologic.apprologic.model.article.Article;
import mg.apprologic.apprologic.model.bons.*;
import mg.apprologic.apprologic.model.stock.StockFille;
import mg.apprologic.apprologic.model.stock.StockMere;
import mg.apprologic.apprologic.services.article.ArticleService;
import mg.apprologic.apprologic.services.article.DeviseService;
import mg.apprologic.apprologic.services.bons.BonLivraisonFilleService;
import mg.apprologic.apprologic.services.bons.BonLivraisonMereService;
import mg.apprologic.apprologic.services.fournisseur.FournisseurService;
import mg.apprologic.apprologic.services.stock.StockFilleService;
import mg.apprologic.apprologic.services.stock.StockMereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
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

    @GetMapping("/liste")
    public String getListeBl(Model model)
    {
        model.addAttribute("livraison_liste",bonLivraisonMereService.getAll());
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
    public String saveBl(
                         @ModelAttribute("bonLivrason") BonLivraisonMere bonLivraisonMere
                        ,HttpServletRequest request, RedirectAttributes redirectAttributes)
    {

        List<BonLivraisonFille> bonLivraisonFilleList = new ArrayList<>();
        try {

            //process BL
            bonLivraisonMereService.save(bonLivraisonMere);
            processLignesLivraison(request,bonLivraisonMere,bonLivraisonFilleList);


            //process stock
            StockMere stockMere = new StockMere();
            stockMere.setDescription("Livraison lié au numero :"+bonLivraisonMere.getIdBlMere());
            stockMere.setBonLivraisonMere(bonLivraisonMere);
            stockMere.setDateMouvement(bonLivraisonMere.getDateReception());

            stockMereService.save(stockMere);
            processStock(stockMere,bonLivraisonFilleList);

            redirectAttributes.addFlashAttribute("success","Livraison succes.");
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

    private void processLignesLivraison(HttpServletRequest request, BonLivraisonMere bonLivraisonMere, List<BonLivraisonFille> livraisonFilleList) throws Exception {
        int i = 0;
        while (request.getParameter("lignes"+i+".idArticle") != null)
        {
            String suffixe = "lignes"+i+".";
            Integer idArticle = Integer.valueOf(request.getParameter(suffixe+"idArticle"));

            BonLivraisonFille bonLivraisonFille = new BonLivraisonFille();
            bonLivraisonFille.setBonLivraisonMere(bonLivraisonMere);
            bonLivraisonFille.setArticle(articleService.getById(idArticle));
            bonLivraisonFille.setPrixUnitaire(request.getParameter(suffixe+"PU"));
            bonLivraisonFille.setQuantite_demande(request.getParameter(suffixe+"quantiteDemande"));
            bonLivraisonFille.setQuantite_recu(request.getParameter(suffixe+"quantiteReçu"));



            bonLivraisonFilleService.save(bonLivraisonFille);

            livraisonFilleList.add(bonLivraisonFille);

            i++;
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
