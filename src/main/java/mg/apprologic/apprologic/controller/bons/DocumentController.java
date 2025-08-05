package mg.apprologic.apprologic.controller.bons;



import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mg.apprologic.apprologic.model.bons.BordereauFille;
import mg.apprologic.apprologic.model.bons.BordereauMere;
import mg.apprologic.apprologic.model.utilisateur.Utilisateur;
import mg.apprologic.apprologic.services.bons.BordereauFilleService;
import mg.apprologic.apprologic.services.bons.BordereauMereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ByteArrayResource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
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