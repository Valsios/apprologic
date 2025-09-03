package mg.apprologic.apprologic.controller.bons;



import com.opencsv.CSVWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mg.apprologic.apprologic.model.article.Article;
import mg.apprologic.apprologic.model.bons.BordereauFille;
import mg.apprologic.apprologic.model.bons.BordereauMere;
import mg.apprologic.apprologic.model.stock.StockFille;
import mg.apprologic.apprologic.model.stock.StockMere;
import mg.apprologic.apprologic.model.utilisateur.Utilisateur;
import mg.apprologic.apprologic.services.article.ArticleService;
import mg.apprologic.apprologic.services.bons.BonLivraisonMereService;
import mg.apprologic.apprologic.services.bons.BordereauFilleService;
import mg.apprologic.apprologic.services.bons.BordereauMereService;
import mg.apprologic.apprologic.services.stock.StockFilleService;
import mg.apprologic.apprologic.services.stock.StockMereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ByteArrayResource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring6.expression.ThymeleafEvaluationContext;
import org.xhtmlrenderer.pdf.ITextRenderer;


@RestController
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    TemplateEngine templateEngine;

    @Autowired
    BordereauMereService bordereauMereService ;

    @Autowired
    BordereauFilleService bordereauFilleService;
    @Autowired
    StockFilleService stockFilleService;

    @Autowired
    BonLivraisonMereService bonLivraisonMereService;

    @Autowired
    StockMereService stockMereService;


    @Autowired
    ArticleService articleService;



    @GetMapping("/downloadRepartitionLivraison")
    public ResponseEntity<ByteArrayResource> downloadRepartitionLivraison(String fileName, String content)
    {
        byte[] csvBytes = content.toString().getBytes(StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(csvBytes.length)
                .body(new ByteArrayResource(csvBytes));
    }

    @GetMapping("/viewFileStock/{stockMereId}")
    @Transactional
    public ResponseEntity<byte[]> viewFilestockMere(@PathVariable Integer stockMereId,Authentication authentication) {

        StockMere stockMere = stockMereService.getById(stockMereId);
        System.out.println("STOCK MERE ID : "+stockMere.getIdStockMere());
        byte[] fileContent = null;
        if (stockMere.getDemandeMere() == null)
        {
            fileContent = bonLivraisonMereService.getPieceJointeById(stockMere.getBonLivraisonMere().getIdBlMere());
        }
        if (stockMere.getDemandeMere() != null)
        {
            BordereauMere bordereauMere = bordereauMereService.getBordereauMereByDemande(stockMere.getDemandeMere().getIdDemandeMere());
            List<BordereauFille> bordereauFilles = bordereauFilleService.getAllByMere(bordereauMere);
            try {
                // 2. Create web context - THE CORRECT WAY
                Context context = new Context();
                context.setVariable("mere", bordereauMere);
                context.setVariable("filles", bordereauFilles);
                context.setVariable("total",BordereauMere.getTotal(bordereauFilles));

                Utilisateur auth = (Utilisateur)authentication.getPrincipal();
                context.setVariable("userName", auth.getNom() + " "+auth.getPrenom());
                //process the page
                String htmlContent = templateEngine.process("bordereau", context);


                // 6. Generate PDF (keep your existing PDF generation code)
                ITextRenderer renderer = new ITextRenderer();
                renderer.setDocumentFromString(htmlContent);
                renderer.layout();

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                renderer.createPDF(outputStream);
                fileContent = outputStream.toByteArray();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return ResponseEntity.internalServerError().build();
            }

        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .contentType(MediaType.APPLICATION_PDF)
                .body(fileContent);
    }


    @PostMapping("/exportStockAnnuel")
    @Transactional
    public ResponseEntity<ByteArrayResource> downloadStockArticleExcel(@RequestParam Integer currentYear,@RequestParam Integer idArticle) {

        Article article = articleService.getById(idArticle);
        List<StockFille> stockFilleList = stockFilleService.getStockFilleByArticleAndYearWithoutAnomalie(article,currentYear);

        // 1. Créer le contenu CSV
        StringBuilder csvContent = new StringBuilder();
        csvContent.append(StockFille.getColumnForArticle()).append("\n"); // Entête
        stockFilleList.forEach(stock -> csvContent.append(stock.toStringArticleStock()).append("\n"));

        // 2. Convertir en bytes (UTF-8 important)
        byte[] csvBytes = csvContent.toString().getBytes(StandardCharsets.UTF_8);

        // 3. Créer la réponse
        String fileName = "stockArticle_"+article.getDesignation()+"_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(csvBytes.length)
                .body(new ByteArrayResource(csvBytes));
    }
    @GetMapping("/download-stock-excel")
    public ResponseEntity<ByteArrayResource> downloadStockExcel() {
        List<StockFille> stockFilleList = stockFilleService.stock_date(null, null);

        // 1. Créer le contenu CSV
        StringBuilder csvContent = new StringBuilder();
        csvContent.append(StockFille.getColumn()).append("\n"); // Entête
        stockFilleList.forEach(stock -> csvContent.append(stock.toStringStock()).append("\n"));

        // 2. Convertir en bytes (UTF-8 important)
        byte[] csvBytes = csvContent.toString().getBytes(StandardCharsets.UTF_8);

        // 3. Créer la réponse
        String fileName = "stock_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(csvBytes.length)
                .body(new ByteArrayResource(csvBytes));
    }

    @PostMapping("/generer-bordereau")
    public ResponseEntity<ByteArrayResource> genererPdf(
            @RequestParam Integer idBordereau,
            HttpServletRequest request, HttpServletResponse response,
            Authentication authentication
            ) {

        // 1. Get data from services
        BordereauMere bordereauMere = bordereauMereService.getById(idBordereau);
        List<BordereauFille> bordereauFilles = bordereauFilleService.getAllByMere(bordereauMere);

        try {
            // 2. Create web context - THE CORRECT WAY
            Context context = new Context();
            context.setVariable("mere", bordereauMere);
            context.setVariable("filles", bordereauFilles);
            context.setVariable("total",BordereauMere.getTotal(bordereauFilles));

            Utilisateur auth = (Utilisateur)authentication.getPrincipal();
            context.setVariable("userName", auth.getNom() + " "+auth.getPrenom());
            //process the page
            String htmlContent = templateEngine.process("bordereau", context);


            // 6. Generate PDF (keep your existing PDF generation code)
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            renderer.createPDF(outputStream);
            byte[] pdfBytes = outputStream.toByteArray();

            // 7. Return PDF response
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=bordereau_" + bordereauMere.getIdBordereauMere() + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new ByteArrayResource(pdfBytes));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}