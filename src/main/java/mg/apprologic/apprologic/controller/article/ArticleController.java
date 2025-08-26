package mg.apprologic.apprologic.controller.article;

import jakarta.servlet.http.HttpServletRequest;
import mg.apprologic.apprologic.model.article.Article;
import mg.apprologic.apprologic.services.article.ArticleService;
import mg.apprologic.apprologic.services.article.FamilleService;
import mg.apprologic.apprologic.services.article.UdmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    ArticleService articleService;

    @Autowired
    UdmService udmService;

    @Autowired
    FamilleService familleService;

    //search criteria
    @PostMapping("/search")
    @ResponseBody
    public List<Article> searchArticles(
            @RequestParam String query,
            @RequestParam String searchType) {

        switch(searchType) {
            case "code":
                return articleService.getByCodeArticle(query);
            case "designation":
                return articleService.getByDesignation(query);
            default:
                return new ArrayList<Article>();
        }
    }

    @GetMapping("/searchContaining")
    @ResponseBody
    public List<Article> searchArticlesContaining(@RequestParam String q) {
        return articleService.getByContaining(q);
    }

    // Afficher la liste
    @GetMapping("/liste")
    public String listeArticle(Model model, HttpServletRequest request)
    {
        model.addAttribute("liste_article",articleService.getAll());
        return "article/ListeArticle";
    }

    // Afficher le formulaire d'ajout
    @GetMapping("/ajout")
    public String ajoutArticle(Model model, HttpServletRequest request)
    {
        model.addAttribute("article",new Article());
        model.addAttribute("udmList",udmService.getAll());
        model.addAttribute("familleList",familleService.getAll());
        return "article/AjoutArticle";
    }

    // Afficher le formulaire d'édition
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model, HttpServletRequest request) {


        Article article = articleService.getById(id);
        model.addAttribute("article", article);
        return "article/EditArticle";
    }

    // update or save
    @PostMapping("/updateOrSave")
    public String updateOrSaveArticle(@ModelAttribute("article") Article article,
                                HttpServletRequest request) {

        articleService.save(article);
        return "redirect:/article/liste";
    }

}
