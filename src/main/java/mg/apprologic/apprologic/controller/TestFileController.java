package mg.apprologic.apprologic.controller;

import jakarta.servlet.http.HttpServletRequest;
import mg.apprologic.apprologic.model.TestFile;
import mg.apprologic.apprologic.model.bons.BonLivraisonFille;
import mg.apprologic.apprologic.model.bons.BonLivraisonMere;
import mg.apprologic.apprologic.model.stock.StockFille;
import mg.apprologic.apprologic.model.stock.StockMere;
import mg.apprologic.apprologic.repository.TestFileRepository;
import mg.apprologic.apprologic.services.article.ArticleService;
import mg.apprologic.apprologic.services.article.DeviseService;
import mg.apprologic.apprologic.services.bons.BonLivraisonFilleService;
import mg.apprologic.apprologic.services.bons.BonLivraisonMereService;
import mg.apprologic.apprologic.services.fournisseur.FournisseurService;
import mg.apprologic.apprologic.services.stock.StockFilleService;
import mg.apprologic.apprologic.services.stock.StockMereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/testFile")
public class TestFileController {
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
    TestFileRepository testFileRepository;
    @GetMapping("/form")
    public String getForm(Model model)
    {
        model.addAttribute("testFile",new TestFile());
        model.addAttribute("bonLivraison",new BonLivraisonMere());
        model.addAttribute("fournisseur_liste",fournisseurService.getAll());
        model.addAttribute("devise_liste",deviseService.getAll());
        return "TestFile";
    }


    @PostMapping("/save")
    public String save(@RequestParam("file") MultipartFile file, @ModelAttribute("bonLivraison") BonLivraisonMere bonLivraisonMere) throws IOException {
        TestFile testFile = new TestFile();
        testFile.setFile(file.getBytes());

        bonLivraisonMere.setPieceJointe(testFile.getFile());
        bonLivraisonMereService.save(bonLivraisonMere);

        testFileRepository.save(testFile);
        return "TestFile";
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
