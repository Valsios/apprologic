package mg.apprologic.apprologic.controller.notification;

import mg.apprologic.apprologic.services.stock.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.Map;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;
    @PostMapping("/mark-as-read")
    public String markAsRead() {
        notificationService.markAllAsRead();
        return "redirect:/";
    }
    @GetMapping("/count")
    @ResponseBody
    public Map<String, Integer> getNotificationCount() {
        return Collections.singletonMap("count", notificationService.countUnreadNotifications());
    }
}