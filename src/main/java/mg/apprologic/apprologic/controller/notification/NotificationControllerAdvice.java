package mg.apprologic.apprologic.controller.notification;

import mg.apprologic.apprologic.services.stock.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class NotificationControllerAdvice {

    @Autowired
    private NotificationService notificationService;

    @ModelAttribute
    public void addNotifications(Model model) {
        model.addAttribute("unreadCount", notificationService.countUnreadNotifications());
        model.addAttribute("notifications", notificationService.getUnreadNotifications());
    }
}
