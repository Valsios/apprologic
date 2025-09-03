package mg.apprologic.apprologic.controller.custom;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.Date;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // Récupérer le code d'erreur
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            // Personnaliser le message selon le code d'erreur
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("errorTitle", "Page non trouvée (404)");
                model.addAttribute("errorMessage", "La page que vous recherchez n'existe pas.");
                model.addAttribute("errorCode", "404");
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("errorTitle", "Erreur interne du serveur (500)");
                model.addAttribute("errorMessage", "Une erreur s'est produite côté serveur.");
                model.addAttribute("errorCode", "500");
            } else if (statusCode == HttpStatus.BAD_REQUEST.value()) {
                model.addAttribute("errorTitle", "Requête incorrecte (400)");
                model.addAttribute("errorMessage", "La requête envoyée au serveur est incorrecte.");
                model.addAttribute("errorCode", "400");
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                model.addAttribute("errorTitle", "Accès refusé (403)");
                model.addAttribute("errorMessage", "Vous n'avez pas l'autorisation d'accéder à cette ressource.");
                model.addAttribute("errorCode", "403");
            } else {
                model.addAttribute("errorTitle", "Erreur " + statusCode);
                model.addAttribute("errorMessage", "Une erreur s'est produite.");
                model.addAttribute("errorCode", statusCode);
            }
        } else {
            model.addAttribute("errorTitle", "Erreur");
            model.addAttribute("errorMessage", "Une erreur s'est produite.");
            model.addAttribute("errorCode", "Inconnu");
        }
        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("path", request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI));
        return "error/ErrorPage"; // Votre template d'erreur
    }
}